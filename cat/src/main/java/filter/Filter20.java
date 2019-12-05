package filter;

import com.alibaba.fastjson.JSONObject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
//注解/过滤器名称/该过滤器对所有请求有效
@WebFilter(filterName = "Filter 20",urlPatterns = "/*")
public class Filter20 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //打印提示信息
        System.out.println("access authorization verification starts");
        //强制类型转换成HttpServletRequest类型
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        //强制类型转换成HttpServletRequest类型
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获得请求的url
        String path = request.getRequestURI();
        JSONObject message = new JSONObject();
        if (!path.contains("/login.ctl")) {
            //访问权限验证
            HttpSession session = request.getSession(false);
            //如果session不存在，则是用户没有登陆或超时
            //如果session存在，但是没有currentUser的属性，则是通过其他方式创建了session,而非登录功能
            if (session == null || session.getAttribute("currentUser") == null){
                response.setContentType ("text/html ;charset=UTF-8");
                message.put("message","请登录或重新登陆");
                //响应message到前端
                response.getWriter().println(message);
                return;
            }
        }else {response.setContentType ("text/html ;charset=UTF-8" );
            System.out.println("设置响应对象字符编码格式为utf8");
            request.setCharacterEncoding("UTF-8");
            System.out.println("设置请求对象字符编码格式为utf8" );}
        //执行其他过滤器，若过滤器已经执行完毕，则执行原请求
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("access authorization verification ends");
    }
    @Override
    public void destroy() {}
}
