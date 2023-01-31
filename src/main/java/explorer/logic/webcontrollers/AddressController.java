package explorer.logic.webcontrollers;

import explorer.logic.Explorer;
import explorer.logic.data.Txn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.ArrayList;

@Controller
@RequestMapping("/address")
public class AddressController {

    final int pagesize = 50;
    Explorer eth = null;

    @PostMapping
    public String transactionsPOST(
            @RequestParam String address) {
        // Receives the POST request from the Homepage form
        return "redirect:/address/" + address;
    }

    @GetMapping("/{address}")
    public ModelAndView transactionsGET(
            @PathVariable String address,
            @RequestParam(name="sinceBlock", required=false, defaultValue="0") String bl,
            @RequestParam(name="page", required=false, defaultValue="1") String page
    ) throws Exception {

        ModelAndView mav = new ModelAndView("address");      // address.html

        long pag;
        BigInteger block;

        // Validate page number
        try{
            pag = Long.parseLong(page);
        } catch (Exception e) { throw new Exception("Invalid page number.", e); }

        // Validate block number
        try{
            block = new BigInteger(bl);
        } catch (Exception e) { throw new Exception("Invalid block number.", e); }


        // If looking up a different address, or if its the first address
        try{
            if (eth == null || eth.getAddress().compareToIgnoreCase(address) != 0)
                eth = new Explorer(address);   // todo - add ability to save multiple in "cache"
        } catch (Exception e) { throw new Exception("Invalid address.", e); }


        ArrayList<Txn> txns = new ArrayList<>(
//                    eth.getPageOfTransactions(bl, pagesize, pag)
                eth.getTxnsSinceBlock(block)
        );

        String bal = Explorer.getBalance(address, null).toPlainString();

        mav.addObject("transactions", txns.toArray());     // Send array to the View, with the name of "transactions"
        mav.addObject("currBalance", bal);
        mav.addObject("thisAddress", eth.getAddress());

        mav.addObject("pagetitle", address);


        return mav;
    }
}
