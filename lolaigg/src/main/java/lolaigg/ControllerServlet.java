package lolaigg;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "*.do", initParams = {@WebInitParam(name = "initParam", 
																value = "/WEB-INF/commandHandler.properties")})
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	// <요청타입, 핸들러객체> 매핑 정보 저장
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();
	
	// 서블릿을 생성하고 초기화할 때 제일 먼저 자동으로 실행되는 init() 메소드 이용
	public void init(ServletConfig config) throws ServletException {
		String configFile = config.getInitParameter("initParam");
		String configFilePath = config.getServletContext().getRealPath(configFile);
		
		Properties prop = new Properties();
		
		try (FileInputStream fis = new FileInputStream(configFilePath)) {
			prop.load(fis);
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
		// Properties 객체(prop)저장된 설정 파일을 하나씩 읽어서
		// 키(요청패턴)와 실행명령 핸들러객체를 맵에 저장
		Iterator<Object> keyIter = prop.keySet().iterator();
		while(keyIter.hasNext()) {
			
			// command = hello.do를 저장
			String command = (String) keyIter.next();
			
			// handlerClassName = week11.HelloHandler 저장
			String handlerClassName = prop.getProperty(command);
			
			// 문자열 클래스를 실제 클래스 객체로 변환
			try {
				Class<?> handlerClass = Class.forName(handlerClassName);
				CommandHandler handlerInstance = (CommandHandler) handlerClass.newInstance();
				
				commandHandlerMap.put(command, handlerInstance);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}
	
    public ControllerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			process(request, response);
		} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			process(request, response);
		} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ClassNotFoundException, SQLException {
		// 클라이언트 요청을 처리하는 부분
		response.setContentType("text/html;charset=utf-8");
		
		// command = /week11/hello.do 저장
		String command = request.getRequestURI();
		
		command = command.substring(request.getContextPath().length()+1);																					
		// 위 명령문 수행 후 command = hello.do 만 저장
		
		//command 키를 이용해서 핸들러 객체를 가져온다.
		CommandHandler handler = commandHandlerMap.get(command);
		String viewPage = null;
		
		// 인터페이스의 다형성 구현 결과
		viewPage = handler.process(request, response);
		
//	    (AJAX 핸들러처럼 JSON을 직접 응답하고 null을 반환한 경우는 forward 안 함)
		if (viewPage != null) {
		    RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/"+viewPage+".jsp");
		    rd.forward(request, response);
		}
	}
}
