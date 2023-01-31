package explorer.logic.webcontrollers;

import explorer.logic.Explorer;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balance")
public class BalanceRestController {

    @GetMapping(value = "/{address}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getBalance( @PathVariable String address ) throws Exception {

        return getBalanceAtDate(address, null);
    }

    @GetMapping(value = "/{address}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
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

        JSONObject j = new JSONObject().put("resp", resp);

        return j.toString();
    }
}
