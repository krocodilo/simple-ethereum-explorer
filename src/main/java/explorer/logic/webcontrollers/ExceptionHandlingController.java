package explorer.logic.webcontrollers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlingController {

    // Handle any exception
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception e) {
        System.err.println("\n\nRequest: " + req.getRequestURL() + " raised " + e + "\n");
        e.printStackTrace(System.err);

        ModelAndView err = new ModelAndView("error");
        err.addObject("errorMessage", e.getMessage());
        StackTraceElement[] st = e.getStackTrace();

        StringBuilder sb = new StringBuilder("");
        for(StackTraceElement s : st)
            sb.append(s.toString()).append("\n");

        err.addObject("stackTrace", sb);
        err.addObject("url", req.getRequestURL());

        return err;
    }
}
