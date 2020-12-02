package ariman.pact.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InformationController {

    @Autowired
    private ProviderService providerService;

    @RequestMapping("/krrish")
    public String krrish(Model model) {
        Information information = providerService.getInformation();
        model.addAttribute("name", information.getName());
        model.addAttribute("strength", information.getStrength());
        return "krrish";
    }

}
