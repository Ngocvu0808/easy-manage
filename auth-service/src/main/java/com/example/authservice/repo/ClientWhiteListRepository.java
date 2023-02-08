package com.example.authservice.repo;


import com.example.authservice.entities.application.Client;
import com.example.authservice.entities.application.ClientWhiteList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author nguyen
 * @created_date 05/06/2020
 */
public interface ClientWhiteListRepository extends JpaRepository<ClientWhiteList, Integer> {

  Optional<ClientWhiteList> findByIpAndClientIdAndIsDeletedFalse(String ip, Integer clientId);

  List<ClientWhiteList> findAllByClientAndIsDeletedFalse(Client client);

}
