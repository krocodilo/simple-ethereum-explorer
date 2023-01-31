package explorer.logic.webcontrollers;

import explorer.logic.Explorer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @GetMapping({"/{txnHash}"})
    public ModelAndView getBalanceAtDate(
            @PathVariable String txnHash
    ) throws Exception {

        ModelAndView mav = new ModelAndView("transaction"); // transaction.html


        mav.addObject("txn",
                Explorer.getTransactionInfo(txnHash)
        );

        mav.addObject("pagetitle", "TxnTransaction " + txnHash);

        return mav;
    }
}
