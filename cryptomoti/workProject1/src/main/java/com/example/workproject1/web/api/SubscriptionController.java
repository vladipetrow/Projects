package com.example.workproject1.web.api;

import com.example.workproject1.coreServices.MailgunService;
import com.example.workproject1.coreServices.SubscriptionService;
import com.example.workproject1.coreServices.UserService;
import com.example.workproject1.coreServices.models.Agency;
import com.example.workproject1.coreServices.models.Subscription;
import com.example.workproject1.coreServices.models.User;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping()
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final UserService userService;

    public SubscriptionController(SubscriptionService subscriptionService, UserService userService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }
    @PostMapping("/subscribe")
    public Subscription subscribe(@RequestHeader(value = "Authorization") String token) throws SQLException {

        int userId = 0;
        int agencyId = 0;
        String userRole  = userService.getRoleFromToken(token);
        if (userRole.equals("[ROLE_USER]")) {
            userId = userService.getIdFromToken(token);
        } else {
            agencyId = userService.getIdFromToken(token);
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Calendar expiration = Calendar.getInstance();

        expiration.setTime(timestamp);

        expiration.add(Calendar.DAY_OF_WEEK, 30);

        timestamp.setTime(expiration.getTime().getTime());

        Calendar curr_time = Calendar.getInstance();
        if(curr_time.equals(expiration)){
            MailgunService.sendMail("Subscription expired.", "You subscription is about to expire!" +
                    " Please renew it!","vladi.petrow@abv.bg");
        }

        return subscriptionService.createSubscription(userId, agencyId, timestamp);
    }

    @GetMapping("/get/subscription/{id}")
    public Subscription getSubscriptionId(@PathVariable int id) {
        return subscriptionService.getSubscriptionId(id);
    }

    @DeleteMapping("/delete/subscription/{id}")
    public void deleteSubscription(@PathVariable int id){
        subscriptionService.deleteSubscription(id);
    }

    @GetMapping("/list/agency/subscriptions/{agency_id}")
    public List<Agency> listSubscribedAgencyByID(@PathVariable int agency_id) {
        return subscriptionService.listSubscribedAgencyByID(agency_id);
    }
    @GetMapping("/list/user/subscriptions/{user_id}")
    public List<User> listSubscribedUserByID(@PathVariable int user_id) {
        return subscriptionService.listSubscribedUserByID(user_id);
    }
}
