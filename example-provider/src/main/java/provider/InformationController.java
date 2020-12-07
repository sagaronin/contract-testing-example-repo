package provider;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import provider.ulti.Nationality;

@RestController
public class InformationController {

    private Information information = new Information();

    @RequestMapping("/information")
    public Information information(@RequestParam(value="name", defaultValue="Bheem") String name) {
        if (name.equals("Bheem")) {
            HashMap contact = new HashMap<String, String>();
            contact.put("Email", "chota.bheem@dholakpur.com");
            contact.put("Phone Number", "9090950980");
            information.setNationality(Nationality.getNationality());
            information.setContact(contact);
            information.setName("Chota Bheem");
            information.setSalary(4000);
            information.setStrength("Punches");
            information.setWeakness("Laddoos");
        } else if (name.equals("Krrish")) {
            HashMap contact = new HashMap<String, String>();
            contact.put("Email", "krrish.mehra@shimla.com");
            contact.put("Phone Number", "9090940123");
            information.setNationality(Nationality.getNationality());
            information.setContact(contact);
            information.setName("Krrish");
            information.setSalary(80000);
            information.setStrength("Flying");
            information.setWeakness("super-power");
        } else {
            information.setNationality("Unknown Galaxy");
            information.setContact(null);
            information.setName("sAlien");
            information.setSalary(0);
            information.setStrength("UFO");
            information.setWeakness("MIB");
        }

        return information;
    }
}
