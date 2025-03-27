package com.admin.service;

import com.admin.entity.DealerInfoEntity;
import com.admin.exception.CustomException;

import com.admin.repository.DealerInfoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class LoginService {


    private final DealerInfoRepository dealerInfoRepository;
    private final PasswordService passwordService;
    private final EmailService emailService;
    public static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginService(DealerInfoRepository dealerInfoRepository,PasswordService passwordService,EmailService emailService) {
        this.dealerInfoRepository=dealerInfoRepository;
        this.passwordService=passwordService;
        this.emailService=emailService;
    }
    public List<DealerInfoEntity> getDealer()
    {
        return dealerInfoRepository.findAll();
    }

    private static final   String  DEALER_WITHID
            ="Dealer with ID ";
    private  static final  String DEALER_WITHID_NOT_FOUND
            = "not found";

    public String saveDealer(DealerInfoEntity dealerInfoEntity) {
        try {
            logger.debug("Saving dealer: {}", dealerInfoEntity.getUsername());

            DealerInfoEntity dealerInfoEntity1 = new DealerInfoEntity();
            dealerInfoEntity1.setUsername(dealerInfoEntity.getUsername());
            dealerInfoEntity1.setEmail(dealerInfoEntity.getEmail());
            dealerInfoEntity1.setPhoneNumber(dealerInfoEntity.getPhoneNumber());


            String uname = dealerInfoEntity1.getUsername();
            String email = dealerInfoEntity1.getEmail();
            String password = passwordService.generateRandomPassword();



            logger.debug("Password: {}",password);
            dealerInfoEntity1.setPassword(passwordEncoder.encode(password));

            DealerInfoEntity savedDealer = dealerInfoRepository.save(dealerInfoEntity1);
            logger.debug("id valude {}",savedDealer.getId());

            logger.debug("Successfully saved dealer: {}", savedDealer.getUsername());
            Long dealerId = savedDealer.getId();
           if( emailService.sendLoginCredentials(email,uname,password,dealerId))
           {

               return "Dealer added successfully";
           }
           else{
               dealerInfoRepository.deleteById(dealerId);
               return "Dealer Not added Successfully";

           }



        } catch (Exception e) {
            logger.error("Error adding credential: {}", e.getMessage());
            throw new CustomException("Error while adding credential to the table");
        }
    }

        public String validateDealer(DealerInfoEntity dealerInfoEntity) {
            try {
                //optional
                Optional<DealerInfoEntity> optionalDealerInfoEntity ;
                optionalDealerInfoEntity=dealerInfoRepository.findById(dealerInfoEntity.getId());

                if (!optionalDealerInfoEntity.isPresent()) {
                    return "There is no such dealer";
                }
                DealerInfoEntity dealerInfoEntity1 = optionalDealerInfoEntity.get();


                logger.debug("Entered password: {}", dealerInfoEntity.getPassword());
                logger.debug("Stored password: {}", dealerInfoEntity1.getPassword());

                if (dealerInfoEntity1.getUsername()!=null && passwordEncoder.matches(dealerInfoEntity.getPassword(), dealerInfoEntity1.getPassword()) && dealerInfoEntity1.getUsername().equals(dealerInfoEntity.getUsername()) ){
                    return "Login Successful";
                } else {
                    logger.debug("Wrong entity 1{}",dealerInfoEntity1.getPassword());
                    logger.debug("Wrong entity 2{}",dealerInfoEntity);
                    logger.debug("password 2 {}",passwordEncoder.matches(dealerInfoEntity.getPassword(),dealerInfoEntity1.getPassword()));
                    return "Login Credential Failed";
                }
            } catch (Exception e) {
                logger.error("Error during Validation: {}", e.getMessage());
                throw new CustomException("Login authentication failed due to an error: " );
            }
        }


    public String deleteDealer(Long id) {
        try{
            dealerInfoRepository.deleteById(id);
            return "Dealer deleted successfully";
        }
        catch (Exception e)
        {
            throw new CustomException("Error during delete Operation"+e.getMessage());
        }


    }

    public String resetPassword(String password, Long id, String newPassword) {
        try {


            Optional<DealerInfoEntity> optionalDealerInfoEntity;
            optionalDealerInfoEntity = dealerInfoRepository.findById(id);

            if (!optionalDealerInfoEntity.isPresent()) {
                logger.debug("Dealefrnot found");
                return "Dealer Not Found";
            }
            DealerInfoEntity dealerInfoEntity=optionalDealerInfoEntity.get();

            String bcryptPassword = dealerInfoEntity.getPassword();
            logger.debug("bcryptPassword {}",bcryptPassword);
            logger.debug("Password {}",password);




            if (passwordEncoder.matches(password, bcryptPassword)|| password.equals(bcryptPassword)) {
                dealerInfoEntity.setPassword(passwordEncoder.encode(newPassword));
                dealerInfoRepository.save(dealerInfoEntity);
                return "Renamed Succesfully";
            }
            else{
                return "Not verified correctly";
            }

        }
        catch (Exception e)
        {
            throw new CustomException("THis is not hte given password we give you"+e.getMessage());

        }






    }



    public String forgotPassword(Long id , String email)
    {
        Optional<DealerInfoEntity> dealerInfo=dealerInfoRepository.findById(id);
         if(!dealerInfo.isPresent()){
                return "Dealer not found";
         }
         DealerInfoEntity dealer= dealerInfo.get();
         if((dealer.getEmail().equals(email)))
         {



             emailService.sendForgotPassword(email,passwordService.generateRandomPassword());
             return "Correct credetntials";
         }
         return null;




    }


    public DealerInfoEntity updateDealer(Long id, DealerInfoEntity dealer) {
        return dealerInfoRepository.findById(id).map(existingDealer -> {
            existingDealer.setUsername(dealer.getUsername());
            existingDealer.setEmail(dealer.getEmail());
            existingDealer.setPhoneNumber(dealer.getPhoneNumber());
            return dealerInfoRepository.save(existingDealer);
        }).orElseThrow(() -> new EntityNotFoundException(DEALER_WITHID+ id +DEALER_WITHID_NOT_FOUND));
    }
}
