package com.lambdaschool.foundation;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.lambdaschool.foundation.models.*;
import com.lambdaschool.foundation.services.PotluckService;
import com.lambdaschool.foundation.services.RoleService;
import com.lambdaschool.foundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

/**
 * SeedData puts both known and random data into the database. It implements CommandLineRunner.
 * <p>
 * CoomandLineRunner: Spring Boot automatically runs the run method once and only once
 * after the application context has been loaded.
 */
@Transactional
@ConditionalOnProperty(
    prefix = "command.line.runner",
    value = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@Component
public class SeedData
    implements CommandLineRunner
{
    /**
     * Connects the Role Service to this process
     */
    @Autowired
    RoleService roleService;

    /**
     * Connects the user service to this process
     */
    @Autowired
    UserService userService;

    @Autowired
    PotluckService potluckService;

    /**
     * Generates test, seed data for our application
     * First a set of known data is seeded into our database.
     * Second a random set of data using Java Faker is seeded into our database.
     * Note this process does not remove data from the database. So if data exists in the database
     * prior to running this process, that data remains in the database.
     *
     * @param args The parameter is required by the parent interface but is not used in this process.
     */
    @Transactional
    @Override
    public void run(String[] args) throws
                                   Exception
    {
        userService.deleteAll();
        roleService.deleteAll();
        Role r1 = new Role("admin");
        Role r2 = new Role("user");
        Role r3 = new Role("data");

        r1 = roleService.save(r1);
        r2 = roleService.save(r2);
        r3 = roleService.save(r3);

        // admin, data, user
        User u1 = new User("admin",
            "password");
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));
        u1.getRoles()
            .add(new UserRoles(u1,
                r2));
        u1.getRoles()
            .add(new UserRoles(u1,
                r3));

        u1 = userService.save(u1);

        // data, user
        User u2 = new User("cinnamon",
            "1234567");
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));
        u2.getRoles()
            .add(new UserRoles(u2,
                r3));
        userService.save(u2);

        // user
        User u3 = new User("barnbarn",
            "ILuvM4th!");
        u3.getRoles()
            .add(new UserRoles(u3,
                r2));
        userService.save(u3);

        User u4 = new User("puttat",
            "password");
        u4.getRoles()
            .add(new UserRoles(u4,
                r2));
        userService.save(u4);

        User u5 = new User("misskitty",
            "password");
        u5.getRoles()
            .add(new UserRoles(u5,
                r2));
        userService.save(u5);

//        if (false)
//        {
//            // using JavaFaker create a bunch of regular users
//            // https://www.baeldung.com/java-faker
//            // https://www.baeldung.com/regular-expressions-java
//
//            FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"),
//                new RandomService());
//            Faker nameFaker = new Faker(new Locale("en-US"));
//
//            for (int i = 0; i < 25; i++)
//            {
//                new User();
//                User fakeUser;
//
//                fakeUser = new User(nameFaker.name()
//                    .username(),
//                    "password",
//                    nameFaker.internet()
//                        .emailAddress());
//                fakeUser.getRoles()
//                    .add(new UserRoles(fakeUser,
//                        r2));
//                fakeUser.getUseremails()
//                    .add(new Useremail(fakeUser,
//                        fakeValuesService.bothify("????##@gmail.com")));
//                userService.save(fakeUser);
//            }
//        }


        Potluck potluck1 = new Potluck("Sunday brunch at Granny's", "12/26/2021", "3:00 pm", "Granny's House");
        potluck1.setUser(u1);
        potluckService.save(potluck1, u1.getUserid());


//        Potluck potluck2 = new Potluck("Easter Dinner", "4/25/2021", "1:00 pm", "Aunt Jan's");
//        Potluck potluck3 = new Potluck("Book Club Meeting", "3/7/2021", "12:00 pm", "Library Meeting Room, 123 Main St");


    }
}