package com.food.OnlineFoodOrdering.Controller;
import org.springframework.web.bind.annotation.RestController;

import com.food.OnlineFoodOrdering.Model.Food;
import com.food.OnlineFoodOrdering.Model.Restaurant;
import com.food.OnlineFoodOrdering.Model.User;
import com.food.OnlineFoodOrdering.request.CreateFoodRequest;
import com.food.OnlineFoodOrdering.response.MessageResponse;
import com.food.OnlineFoodOrdering.service.FoodService;
import com.food.OnlineFoodOrdering.service.RestaurantService;

import com.food.OnlineFoodOrdering.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {


    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;


    @PostMapping()
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest req , 
        @RequestHeader("Authorization") String jwt ) throws Exception{

            User user = userService.findUserByJwtToken(jwt);
            System.out.println(user);
            Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
            Food food=foodService.createFood(req, req.getCategory(), restaurant);


            return new ResponseEntity<>(food,HttpStatus.CREATED);
        }


        @DeleteMapping("/{id}")
        public ResponseEntity<MessageResponse> DeleteFood(
            @RequestHeader("Authorization") String jwt, @PathVariable Long id) throws Exception{
    
                //User user = userService.findUserByJwtToken(jwt);
              
                foodService.deleteFood(id);

                MessageResponse res = new MessageResponse();
                res.setMessage("food deleted successfully");
                return new ResponseEntity<>(res,HttpStatus.CREATED);    
    
               
            }
            @PutMapping("/{id}")
            public ResponseEntity<Food> updateFoodAvailbilityStatus(@PathVariable Long id,
            @RequestHeader("Authorization") String jwt) throws Exception{
                //User user = userService.findUserByJwtToken(jwt);
              
               Food food= foodService.updateAvailibilityStatus(id);
                return new ResponseEntity<>(food,HttpStatus.CREATED);    
    

            }

}
