package services.handlers;


import services.RegisterService;
import services.request.RegisterRequest;
import services.result.LoginRegisterResult;
import spark.Request;
import spark.Response;

/**
 * RegisterHandler interacts with the RegisterService, which will then be interacting with DAO models and the database
 * to register new Users in the system.
 */
public class RegisterHandler extends Handler {
    public String handleRequest(Request request, Response response) {
        RegisterRequest requestObject = (RegisterRequest) gsonToRequest(RegisterRequest.class, request);
        System.out.println(requestObject.toString());
        RegisterService service = new RegisterService();
        LoginRegisterResult resultObject = service.register(requestObject);
        return null;
    }
}
