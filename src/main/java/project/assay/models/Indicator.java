//package project.assay.models;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.Table;
//import java.util.List;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "indicator")
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Indicator {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(name = "id")
//  private int id;
//
//  @Column(name = "name")
//  private String name;
//
//  @Column(name = "gender")
//  private String gender;
//
//  @Column(name = "is_gravid")
//  private boolean isGravid;
//
//  @Column(name = "min_age")
//  private int minAge;
//
//  @Column(name = "max_age")
//  private int maxAge;
//
//  @Column(name = "min_value")
//  private double minValue;
//
//  @Column(name = "max_value")
//  private double maxValue;
//
//  @OneToMany(mappedBy = "indicator", fetch = FetchType.LAZY)
//  private List<PersonInfo> personIndicators;
//}
