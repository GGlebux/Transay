package project.assay.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.assay.dto.requests.UpgradeUserDTO;
import project.assay.dto.responses.CustomerDTO;
import project.assay.dto.responses.MessageDTO;
import project.assay.services.CustomerService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;
import static project.assay.models.Roles.ADMIN;
import static project.assay.models.Roles.USER;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Пользователи", description = "Данные авторизации и спец методы для админа")
@PreAuthorize(USER)
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/all")
    @Operation(summary = "Получить всех пользователей")
    @PreAuthorize(ADMIN)
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ok(customerService.getAllCustomers());
    }

    @PostMapping("/upgrade")
    @Operation(summary = "Сменить роль пользователя")
    @PreAuthorize(ADMIN)
    public ResponseEntity<MessageDTO> upgrade(@RequestBody @Valid UpgradeUserDTO dto) {
        return ok(customerService.updateUserRole(dto));
    }

    @GetMapping
    @Operation(summary = "Получить текущие данные аутентификации")
    public ResponseEntity<CustomerDTO> getCustomer() {
        return ok(customerService.getCurrent());
    }
}
