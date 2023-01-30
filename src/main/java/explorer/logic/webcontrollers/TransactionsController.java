package explorer.logic.webcontrollers;

import explorer.logic.Explorer;
import explorer.logic.models.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;

@Controller
@RequestMapping("/transactions")
public class TransactionsController {

    String address = "";
    final int pagesize = 50;
    Explorer eth;

    @PostMapping
    public String transactionsPOST(
            @RequestParam String address) {
        // Receives the POST request from the Homepage form
        return "redirect:/transactions/" + address;
    }

    @GetMapping("/{address}")
    public String transactionsGET(
            @PathVariable String address,
            @RequestParam(name="sinceBlock", required=false, defaultValue="0") String block,
            @RequestParam(name="page", required=false, defaultValue="1") String page,
            Model model ) throws Exception {

        long pag = 1;
        BigInteger bl;
        try{
            Long.parseLong(page);
            bl = new BigInteger(block);

            if (this.address.compareToIgnoreCase(address) != 0){
                // If looking up a different address
                this.address = address;

                eth = new Explorer(address);

            }
            ArrayList<Transaction> txns = new ArrayList<>(
//                    eth.getPageOfTransactions(bl, pagesize, pag)
                    eth.getTxnsSinceBlock(bl)
            );

            model.addAttribute("transactions", txns.toArray());     // Send array to the View, with the name of "transactions"
            model.addAttribute("thisAddress", eth.getAddress());

            model.addAttribute("pagetitle", address + " transactions");

        } catch (Exception e) {
            throw e;    // Todo create error page
        }
        return "transactions";  // Returns view transactions.html
    }
}
