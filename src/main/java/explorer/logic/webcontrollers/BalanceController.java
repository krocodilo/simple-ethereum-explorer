package explorer.logic.webcontrollers;

import explorer.logic.Explorer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/balance")
public class BalanceController {

    @GetMapping({"/{address}"})
    public String getBalanceAtDate(
            @PathVariable String address,
            @RequestParam(name="date", required=false, defaultValue="1") String date,
            Model model ) throws Exception {

        model.addAttribute("balance",
                Explorer.getBalance(address, "2022-12-01")
        );

        return "balance";  // Returns view balance.html
    }
}
