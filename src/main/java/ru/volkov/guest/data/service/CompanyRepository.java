package ru.volkov.guest.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.volkov.guest.data.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}