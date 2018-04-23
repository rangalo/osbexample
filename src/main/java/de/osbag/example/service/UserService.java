package de.osbag.example.service;


import de.osbag.example.entity.User;
import de.osbag.example.repository.UserRepository;
import de.osbag.example.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public void saveUser(User user) {

        userRepository.save(user);
    }

    public List<UserResponse> allUsers() {

        List<User> allUsers = new ArrayList<>();

        Iterable<User> all = userRepository.findAll();

        all.forEach(allUsers::add);

        return allUsers.stream().map(this::fromUser).collect(Collectors.toList());
    }

    private UserResponse fromUser(User user) {

        UserResponse resp = new UserResponse();

        resp.setId(user.getId());
        resp.setName(user.getFirstName() + " " + user.getLastName());
        LocalDate dateOfBirth = user.getDateOfBirth();

        if (null != dateOfBirth) {
            Period between = Period.between(dateOfBirth, LocalDate.now());
            resp.setAge(between.getYears());
        }

        resp.setBmi(getBmi(user.getWeightInKg(), user.getHeightInCm()/100.00));

        return resp;
    }

    private double getBmi(double weightInKg, double heightInMeter) {
        return weightInKg / (heightInMeter * heightInMeter);
    }
}
