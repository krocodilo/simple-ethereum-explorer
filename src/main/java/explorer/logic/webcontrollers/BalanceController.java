package explorer.logic.webcontrollers;

import explorer.logic.Explorer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @GetMapping({"/{address}"})
    public String getBalance( @PathVariable String address ) throws Exception {

        // Validate address
        if( ! Explorer.isValidAddress(address) )
            throw new Exception("Invalid address.");

        return getBalanceAtDate(address, null);
    }

    @GetMapping({"/{address}/{date}"})
    public String getBalanceAtDate(
            @PathVariable String address,
            @PathVariable String date
    ) throws Exception {

        // Validate address
        if( ! Explorer.isValidAddress(address) )
            throw new Exception("Invalid address.");

        String resp = "";

        try{
            if(date != null)    // If its an historical balance
                resp = "Balance on that date:   ";
            resp = resp + Explorer.getBalance(address, date).toPlainString() + "  ETH";
        } catch (Exception e){
            resp = e.getMessage();
            e.printStackTrace();
        }

        return "{\"resp\":\"" + resp + "\"}";
    }
}
