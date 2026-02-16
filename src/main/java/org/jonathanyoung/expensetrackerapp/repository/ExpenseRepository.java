package org.jonathanyoung.expensetrackerapp.repository;

import org.jonathanyoung.expensetrackerapp.model.DTO.ExpenseDTO;
import org.jonathanyoung.expensetrackerapp.model.DTO.UserDTO;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends ListCrudRepository<ExpenseDTO, UUID> {

    List<ExpenseDTO> findAllByUser(UserDTO userDTO);
}
