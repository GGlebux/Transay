//package project.assay.models;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Table;
//import java.time.LocalDate;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "referent_info")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Referent {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private int id;
//
//  @Column(name = "current_value")
//  private double currentValue;
//
//  @Column(name = "reg_date")
//  private LocalDate regDate;
//
//  @Column(name = "units")
//  private String units;
//
//  @Column(name = "status")
//  private String status;
//
//
//
//}
