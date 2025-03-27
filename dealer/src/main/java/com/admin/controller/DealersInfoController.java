package com.admin.controller;


import com.admin.entity.DealerInfoEntity;
import com.admin.service.DealerService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RestController
@CrossOrigin
@RequestMapping("/api/admin/dealer")
public class DealersInfoController {

    DealerService dealerService;

    public DealersInfoController(DealerService cdmService) {
        this.dealerService = cdmService;
    }

    @GetMapping("/view")         // view
    public List<DealerInfoEntity> viewDealer(){
        return dealerService.getAllDealers();
    }


    @GetMapping("/view/{id}")        //view/id

    public Optional<DealerInfoEntity> dealerById(@PathVariable Long id)
    {
        return dealerService.dealerById(id);

    }



    @DeleteMapping("/delete/{id}")     //delete/id
    public String deleteDealer(@PathVariable Long id){

        return dealerService.deleteDealer(id);
    }

    @PutMapping("/update/{id}")        //update/id
    public DealerInfoEntity updateDealer(@PathVariable  Long id, @RequestBody DealerInfoEntity c){
        return dealerService.updateDealer(id, c);
    }




    @GetMapping("/maxPurchases")
    public List<Map<String,Object>> getMaxDealerPurchases() {
        return dealerService.getMaximumDealerPurchases();}

}
