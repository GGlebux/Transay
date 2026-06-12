package project.assay.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.assay.dto.requests.UpgradeUserDTO;
import project.assay.dto.responses.CustomerDTO;
import project.assay.dto.responses.MessageDTO;
import project.assay.exceptions.EntityNotFoundException;
import project.assay.exceptions.InvalidOperationException;
import project.assay.models.Customer;
import project.assay.models.Person;
import project.assay.repositories.CustomerRepository;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.EnumSet.of;
import static java.util.Optional.ofNullable;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;
import static project.assay.models.enums.CustomerRole.EDITOR;
import static project.assay.models.enums.CustomerRole.USER;

@Service
@Transactional(readOnly = true)
@EnableScheduling
public class CustomerService {
    private final CustomerRepository repo;
    private final ModelMapper mapper;


    @Autowired
    public CustomerService(CustomerRepository repo,
                           ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public Customer findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Пользователь с id '%s' не найден!"
                                        .formatted(id)));
    }

    public Customer findByEmail(String email){
        return repo.findByEmail(email)
                .orElseThrow(() ->
                    new EntityNotFoundException(
                            "Пользователь с почтой '%s' не найден!"
                                    .formatted(email)));
    }


    public List<CustomerDTO> getAllCustomers(){
        return repo
                .findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public CustomerDTO getCurrent(){
        var customer = this.getAuthenticatedCustomer();
        return mapToDTO(customer);
    }


    @Transactional
    public MessageDTO updateUserRole(UpgradeUserDTO dto){
        if (!of(EDITOR, USER).contains(dto.getRole())) {
            throw new InvalidOperationException(
                    "Пользователя можно повысить только до редактора!");
        }
        var customer = this.findById(dto.getId());
        customer.setRole(dto.getRole());
        save(customer);
        return new MessageDTO(
                        "Пользователь '%s' получил роль '%s'!"
                                .formatted(customer.getEmail(), dto.getRole()));
    }

    @Transactional
    public Customer save(Customer customer){
        return repo.save(customer);
    }

    @Transactional
    public void delete(Customer customer){
        repo.delete(customer);
    }

    private CustomerDTO mapToDTO(Customer customer){
        var dto = mapper.map(customer, CustomerDTO.class);

        ofNullable(customer.getPerson())
                .ifPresent(p ->
                        dto.setPersonId(p.getId()));

        return dto;
    }

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void cleanExpiredPendingCustomers(){
        List<Customer> toDelete = repo
                .findAllExpiredPending(now().minusMinutes(20));

        repo.deleteAll(toDelete);
    }

    public Customer getAuthenticatedCustomer() {
        Authentication authentication = getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InvalidOperationException("Пользователь не аутентифицирован!");
        }

        String username = authentication.getName();

        return this
                .findByEmail(username);
    }

    public Person getPersonFromAuth() {
        Customer customer = getAuthenticatedCustomer();
        Person person = customer.getPerson();

        if (person == null) {
            throw new EntityNotFoundException("Личные данные пользователя не были найдены!\nЗаполните их в личном кабинете!");
        }

        return person;
    }
}
