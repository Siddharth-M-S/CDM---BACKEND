package com.admin.service;


import com.admin.entity.DealerInfoEntity;
import com.admin.exception.CustomException;
import com.admin.repository.DealerInfoRepository;
import com.admin.repository.OrdersRepository;
import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@Service
public class DealerService {


    private OrdersRepository ordersRepository;

    public DealerService(DealerInfoRepository repo, OrdersRepository ordersRepository) {

        this.repo = repo;
        this.ordersRepository = ordersRepository;
    }

    private static final String DEALER_WITHID
            = "Dealer with ID ";
    private static final String DEALER_WITHID_NOT_FOUND
            = "not found";

    private final DealerInfoRepository repo;
    private static final Logger logger = LoggerFactory.getLogger(DealerService.class);


    public List<DealerInfoEntity> getAllDealers() {
        return repo.findAll();
    }

    public String saveDealer(DealerInfoEntity dealer) {
        try {
            repo.save(dealer);
            return "Dealer added successfully";
        } catch (Exception e) {
            throw new CustomException("Failed to save dealer: " + e.getMessage());
        }
    }

    @Transactional
    public DealerInfoEntity updateDealer(Long id, DealerInfoEntity dealer) {
        return repo.findById(id).map(existingDealer -> {
            existingDealer.setUsername(dealer.getUsername());
            existingDealer.setEmail(dealer.getEmail());
            existingDealer.setPhoneNumber(dealer.getPhoneNumber());
            return repo.save(existingDealer);
        }).orElseThrow(() -> new EntityNotFoundException(DEALER_WITHID + id + DEALER_WITHID_NOT_FOUND));
    }

    public Optional<DealerInfoEntity> dealerById(Long id) {
        try {
            return repo.findById(id);
        } catch (DataAccessException e) {
            logger.error("Error accessing the database for dealer Id {} :{} ", id, e.getMessage());

            return Optional.empty();
        }
    }


    public List<Map<String, Object>> getMaximumDealerPurchases() {
        return ordersRepository.getMaxDealerPurchases();
    }

    //
    @Transactional
    public
    String deleteDealer(Long id) {
        try {
            Optional<DealerInfoEntity> dealer = repo.findById(id);
            if (dealer.isEmpty()) {
                throw new EntityNotFoundException(DEALER_WITHID + id + DEALER_WITHID_NOT_FOUND);
            }
            ordersRepository.deleteByDealerInfoEntityId(id);
            repo.deleteById(id);
            return "Dealer deleted successfully";
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(DEALER_WITHID + id + DEALER_WITHID_NOT_FOUND);
        } catch (Exception e) {
            throw new CustomException("Failed to delete dealer: " + e.getMessage());
        }
    }

}


