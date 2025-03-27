package com.admin.controller;

import com.admin.dto.ForgotPass;
import com.admin.dto.ForgotPasswordRequest;
import com.admin.entity.DealerInfoEntity;
import com.admin.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/login")
@CrossOrigin
public class LoginController {

    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/view")
    public ResponseEntity<List<DealerInfoEntity>> getDealer()
    {
     return new ResponseEntity<>(loginService.getDealer(), HttpStatus.OK);
    }
    @PostMapping("/save")
    public  ResponseEntity<String> saveDealer(@RequestBody DealerInfoEntity dealerInfoEntity)
    {
        try{
            return  new ResponseEntity<>(loginService.saveDealer(dealerInfoEntity),HttpStatus.OK);
        }
       catch (Exception e)
       {
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
    @PostMapping("/validate")
    public String validate(@RequestBody DealerInfoEntity dealerInfoEntity)
    {

      return  loginService.validateDealer(dealerInfoEntity);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> dealerDelete(@PathVariable Long id)
    {
        try{

            return new ResponseEntity<>(loginService.deleteDealer(id),HttpStatus.OK);
        }
        catch (Exception e)
        {
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/resetPassword")
    public String forgotPass(@RequestBody ForgotPasswordRequest forgotPasswordRequest)
    {

        return loginService.resetPassword(forgotPasswordRequest.getOldPassword(),forgotPasswordRequest.getDealerId(),forgotPasswordRequest.getNewPassword());
    }
    @PutMapping("/update/{id}")        //update/id
    public DealerInfoEntity updateDealer(@PathVariable  Long id, @RequestBody DealerInfoEntity c){
        return loginService.updateDealer(id, c);
    }

    @PutMapping("/forgotPassword")
    public String forgotPassword(@RequestBody ForgotPass forgotPass)
    {
        return  loginService.forgotPassword(forgotPass.getDealerId(),forgotPass.getDealerEmail());
    }


}
