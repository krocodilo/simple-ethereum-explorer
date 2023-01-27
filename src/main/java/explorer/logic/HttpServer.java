package explorer.logic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class HttpServer {

    private String address = "";
    private ArrayList<String> txns;
    private final int txnsNumRowsPerPage = 100;

    @GetMapping({"/"})
    public String index() {
        return "index";
    }

    @GetMapping("/transactions/{address}")
    public String transactionsGET(
            @PathVariable String address,
            @RequestParam(name="page", required=false, defaultValue="1") String page,
            Model model ) {

        long pag = Long.parseLong(page);

        if (this.address.compareToIgnoreCase(address) != 0){
            this.address = address;
            txns = new ArrayList<>();
            // TODO fetch transactions

            for(char c : address.toCharArray())
                txns.add(String.valueOf(c));
        }

        txns.add("request");

        model.addAttribute("transactions", txns.toArray());     // Send array to the View, with the name of "transactions"

        return "transactions";  // Returns view transactions.html
    }

    @PostMapping("/transactions")
    public String transactionsPOST(
            @RequestParam String address) {

        return "redirect:/transactions/" + address;
    }

    @GetMapping({"/balance/{address}"})
    public String getBalanceAtDate(
            @PathVariable String address,
            @RequestParam(name="date", required=false, defaultValue="1") String date,
            Model model ) {

        return "balance";  // Returns view balance.html
    }
}
