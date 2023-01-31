package explorer.logic.webcontrollers;

import explorer.logic.Explorer;
import explorer.logic.data.Txn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigInteger;
import java.util.ArrayList;

@Controller
@RequestMapping("/address")
public class AddressController {

//    final int pagesize = 50;
    Explorer eth = null;

    @PostMapping
    public RedirectView transactionsPOST(
            @RequestParam String address,
            @RequestParam(name="sinceBlock", required=false) String block,
            RedirectAttributes red
    ) {
        // Receives the POST request from the Homepage form or navbar search form
        if( block == null || block.isBlank())
            block = "0";

        red.addAttribute("sinceBlock", block);
        return new RedirectView("address/"+address);
    }

    @GetMapping("/{address}")
    public ModelAndView transactionsGET(
            @PathVariable String address,
            @RequestParam(name="sinceBlock", required=false, defaultValue="0") String bl,
            @RequestParam(name="page", required=false, defaultValue="1") String page
    ) throws Exception {

        ModelAndView mav = new ModelAndView("address");      // address.html

        // Validate page number
//        long pag;
//        try{
//            pag = Long.parseLong(page);
//        } catch (Exception e) { throw new Exception("Invalid page number.", e); }

        // Validate block number
        BigInteger block;
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
