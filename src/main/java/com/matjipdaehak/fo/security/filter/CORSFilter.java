package com.matjipdaehak.fo.security.filter;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import org.slf4j.*;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * CORS에 필요한 정보들을 response에 입력하며,
 * preflight요청에 맞는 대응을 한다. <br/>
 * Preflight 요청의 경우 CORS가 가능한지 확인하는 요청이기 때문에 어떤것이 가능한지 response헤더에 명시하여 보내주면 된다. <br/>
 * 실제적인 작업을 하는 요청이 아니므로 더이상의 작업을 하지 않도록한다.(이 다음의 필터체인으로 넘어가지 않는다.)
 * 이러한 이유 때문에 다른 인증을 하는 필터들보다 앞서서 실행되어야한다.
 */
public class CORSFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        /**
         * CORS 요청에서 사용할 수 있는 정보를 알려준다.
         * OPTION요청이 아닌경우에도 해당 정보가 필요함을 확인함.
         */
        res.setHeader("Access-Control-Allow-Origin","*");
        res.setHeader("Access-Control-Expose-Headers", "*");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Methods", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");

        /**
         * OPTION요청인 경우 preflight 요청이므로 CORS정보만 전달하면 된다.
         * 따라서 정보를 입력한 이 필터 이후의 작업이 필요 X.
         * 바로 reponse전달.
         */
        if(req.getMethod().equals("OPTIONS")){
            logger.info("options request");
            return;
        }

        /**
         * OPTION이 아닌경우 바로 다음 필터체인으로 이동
         */
        filterChain.doFilter(req, res);
    }
}
