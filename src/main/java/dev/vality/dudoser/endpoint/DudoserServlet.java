package dev.vality.dudoser.endpoint;

import dev.vality.damsel.message_sender.MessageSenderSrv;
import dev.vality.woody.api.event.CompositeServiceEventListener;
import dev.vality.woody.thrift.impl.http.THServiceBuilder;
import dev.vality.woody.thrift.impl.http.event.HttpServiceEventLogListener;
import dev.vality.woody.thrift.impl.http.event.ServiceEventLogListener;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@RequiredArgsConstructor
@WebServlet("/dudos")
public class DudoserServlet extends GenericServlet {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private Servlet thriftServlet;

    private final MessageSenderSrv.Iface requestHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.info("Dudoser servlet init.");
        super.init(config);
        thriftServlet = new THServiceBuilder()
                .withEventListener(
                        new CompositeServiceEventListener(
                                new ServiceEventLogListener(),
                                new HttpServiceEventLogListener()))
                .build(MessageSenderSrv.Iface.class, requestHandler);
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        log.info("Start new request to servlet.");
        thriftServlet.service(req, res);
    }
}
