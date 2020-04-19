package com.chinobot.common.websocket.listener;

import static io.github.biezhi.ome.OhMyEmail.SMTP_QQ;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chinobot.cityle.base.entity.Person;
import com.chinobot.cityle.base.entity.Role;
import com.chinobot.cityle.base.entity.Scene;
import com.chinobot.cityle.base.entity.SceneTask;
import com.chinobot.cityle.base.entity.SceneTaskRole;
import com.chinobot.cityle.base.entity.UserRole;
import com.chinobot.cityle.base.service.IPersonService;
import com.chinobot.cityle.base.service.IRoleService;
//import com.chinobot.cityle.base.service.ISceneService;
import com.chinobot.cityle.base.service.ISceneTaskRoleService;
import com.chinobot.cityle.base.service.ISceneTaskService;
import com.chinobot.cityle.base.service.IUserRoleService;
import com.chinobot.cityle.warning.entity.PushMessage;
//import com.chinobot.cityle.warning.service.IPushMessageService;
import com.chinobot.common.constant.GlobalConstant;
import com.chinobot.common.email.OhMyEmail;
import com.chinobot.common.email.SendMailException;
import com.chinobot.framework.web.event.KafkaMsgEvent;
import com.chinobot.framework.web.service.IKafkaDataService;

import lombok.extern.slf4j.Slf4j;

/**
 * kafka消息中间件推送到webSocket
 */
//@Component
@Slf4j
public class KafkaDataListener {

	@Autowired
	private IKafkaDataService kafkaDataService;
//	@Autowired
//	private IPushMessageService pushMessageService;
//	@Autowired
//	private ISceneService sceneService;
	@Autowired
	private ISceneTaskService sceneTaskServie;
	@Autowired
	private IPersonService personServie;
	@Autowired
	private IUserRoleService userRoleService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private ISceneTaskRoleService sceneTaskRoleService;

	@EventListener
	public void pushWebSocket(KafkaMsgEvent kafkaMsgEvent) {

		JSONObject jsonObject = new JSONObject();

		jsonObject = JSON.parseObject(kafkaMsgEvent.getRecord().value());

//      log.debug("推送信息，管道："+kafkaMsgEvent.getRecord ().key ()+":"+jsonObject);

		String uuid = kafkaDataService.updateDataBase(kafkaMsgEvent.getRecord().key(), jsonObject);
		
		// 判断接受到的信息为预警
//		if (kafkaMsgEvent.getRecord().key().equals("task_warning")) {
//			if (jsonObject.get("isWarning").equals("1")) { // 判断是否预警
//				PushMessage pushMessage = new PushMessage();
//				pushMessage.setPushStatus("0");
//				pushMessage.setPusherId("1");
//				pushMessage.setPushWay("1");
//				pushMessage.setPushNumber("0");
//				pushMessage.setWarningId(uuid);
//				pushMessage.setPushTime(LocalDateTime.now());
//				pushMessageService.save(pushMessage);// 推送信息保存
////	        	pushWarning(kafkaMsgEvent,pushMessage.getUuid());//预警推送
//			}
//		}
	}

	/**
	 * 预警信息推送
	 * 
	 * @param kafkaMsgEvent
	 * @param pushMessageId
	 */
//	public void pushWarning(KafkaMsgEvent kafkaMsgEvent, String pushMessageId) {
//		JSONObject jsonObject = new JSONObject();
//		Map pushMap = new HashMap();// 存放推送的预警信息
//		System.out.println("-------" + kafkaMsgEvent.getRecord().key());
//		// 判断接受到的信息为预警信息
//		if (kafkaMsgEvent.getRecord().key().equals("task_warning")) {
//			String value = kafkaMsgEvent.getRecord().value();
//			jsonObject = JSON.parseObject(kafkaMsgEvent.getRecord().value());
//			// 预警信息才会推送，不是预警信息不推送
//			if (jsonObject.get("isWarning").equals("1")) {
//				String warnContent = (String) jsonObject.get("warnContent");// 预警信息
//				pushMap.put("warnContent", warnContent);
//				String warnTime = (String) jsonObject.get("operateTime");// 发生预警的时间
//				pushMap.put("warnTime", warnTime);
//				String sceneUuid = (String) jsonObject.get("sceneUuid");// 场景主键-->根据场景主键查出场景名称与地址
//				Scene scene = getScene(sceneUuid);
//				String warnName = scene.getSname();// 发生预警的场景名称
//				pushMap.put("warnName", warnName);
//				String warnAddress = scene.getAddress();// 发生预警的地点
//				pushMap.put("warnAddress", warnAddress);
//				String sceneTaskUuid = (String) jsonObject.get("roundsUuid");// 巡查内容主键-->根据巡查内容主键查找出预判处置部门id
//				List<Person> list = getDeptPersons(sceneTaskUuid);
//				List<String> roleName = getRoleByTaskId(sceneTaskUuid);// 根据场景任务id获取角色
//				for (Person person : list) {
//					Role role = getRole(person.getUuid());// 根据人员id获取角色
//					if (role != null && roleName != null) {
//						for (String string : roleName) {
//							if (role.getRname().equals(string)) {// 如果人员相对应的角色和场景任务相对应的角色一致，便推送
//								pushMap.put("email", person.getEmail());
//								pushMap.put("phone", person.getPhone());
//								SceneTask sceneTask = getSceneTask(sceneTaskUuid);// 获取推送方式
//								String pushWay = sceneTask.getPushWay();
//								pushByWay(pushMap, pushWay);// 根据推送方式推送
//							}
//						}
//					}
//				}
//				// 推送之后将推送表的状态改变
//				PushMessage pushMessage = new PushMessage();
//				pushMessage.setPushStatus("1");
//				pushMessage.setConfireStatus("1");
//				pushMessage.setUuid(pushMessageId);
//				pushMessageService.updateById(pushMessage);
//			}
//		}
//
//	}

	/**
	 * 根据场景任务id查询角色id，并根据角色id查询角色名
	 * 
	 * @return
	 */
	private List<String> getRoleByTaskId(String sceneTaskUuid) {
		// 根据角色id查询角色名
		QueryWrapper<SceneTaskRole> queryWrapperRole = new QueryWrapper<SceneTaskRole>();
		queryWrapperRole.eq("data_status", GlobalConstant.DATA_STATUS_VALID).eq("scene_task_id", sceneTaskUuid);
		List<String> l = new ArrayList<String>();
		List<SceneTaskRole> list = sceneTaskRoleService.list(queryWrapperRole);
		if (list.size() > 0) {
			for (SceneTaskRole sceneTaskRole : list) {
				String roleId = sceneTaskRole.getRoleId();
				// 根据角色id查询角色名
				QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
				queryWrapperRole.eq("data_status", GlobalConstant.DATA_STATUS_VALID).eq("uuid", roleId);
				Role role = roleService.getOne(queryWrapper);
				l.add(role.getRname());
			}
		}
		return l;
	}

	/**
	 * 根据推送方式推送
	 */
	private void pushByWay(Map pushMap, String pushWay) {
		if (pushWay.length() > 1) {
			String[] way = pushWay.split(pushWay);
			for (int i = 0; i < way.length; i++) {
				if ("0".equals(way[i])) {
					// 短信推送
					phonePush(pushMap);
				}
				if ("1".equals(way[i])) {
					// 邮件推送
					emailPush(pushMap);
				}
			}
		} else {
			if ("0".equals(pushWay)) {
				// 短信推送
				phonePush(pushMap);
			}
			if ("1".equals(pushWay)) {
				// 邮件推送
				emailPush(pushMap);
			}
		}
	}

	/**
	 * 短信推送
	 * 
	 * @param pushMap
	 */
	private void phonePush(Map pushMap) {
		// 获取文件属性
		InputStream inStream = KafkaDataListener.class.getClassLoader().getResourceAsStream("conf/email.properties");
		Properties prop = new Properties();
		try {
			prop.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String phoneUid = prop.getProperty("phone.uid");
		String phoneKey = prop.getProperty("phone.key");
		String phone = (String) pushMap.get("phone");
		String smsTest = null;
		smsTest = "预警信息：在" + pushMap.get("warnAddress") + "," + pushMap.get("warnContent") + ",请及时处理!预警时间："
				+ pushMap.get("warnTime");

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://sms.webchinese.cn/web_api/");
		post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");// 在头文件中设置转码
		NameValuePair[] data = { new NameValuePair("Uid", phoneUid), // 注册的用户名
				new NameValuePair("Key", phoneKey), // 注册成功后,登录网站使用的密钥
				new NameValuePair("smsMob", phone), // 手机号码
				new NameValuePair("smsText", smsTest) };// 设置短信内容
		post.setRequestBody(data);
		try {
			client.executeMethod(post);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:" + statusCode); // statusCode=200表示请示成功！
		for (Header h : headers) {
			System.out.println(h.toString());
		}
		String result = null;
		try {
			result = new String(post.getResponseBodyAsString().getBytes("gbk"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} // 设置编码格式
		System.out.println(result);
		post.releaseConnection();
	}

	/**
	 * 根据人员id关联查询角色
	 * 
	 * @param personId
	 * @return
	 */
	private Role getRole(String personId) {
		QueryWrapper<UserRole> queryWrapper = new QueryWrapper<UserRole>();
		queryWrapper.eq("data_status", GlobalConstant.DATA_STATUS_VALID).eq("person_id", personId);
		UserRole userRole = userRoleService.getOne(queryWrapper);
		// 根据角色id查询角色名
		QueryWrapper<Role> queryWrapperRole = new QueryWrapper<Role>();
		queryWrapperRole.eq("data_status", GlobalConstant.DATA_STATUS_VALID).eq("uuid", userRole.getUuid());
		Role role = roleService.getOne(queryWrapperRole);
		return role;

	}

	/**
	 * 根据场景主键查出场景名称与地址
	 * 
	 * @return
	 */
//	private Scene getScene(String sceneUuid) {
//		QueryWrapper<Scene> queryWrapper = new QueryWrapper();
//		queryWrapper.select("sname", "address");
//		queryWrapper.eq("uuid", sceneUuid);
//		Scene scene = sceneService.getOne(queryWrapper);
//		return scene;
//	}

	/**
	 * 根绝场景任务id查询出推送方式
	 * 
	 * @param sceneTaskUuid
	 * @return
	 */
	private SceneTask getSceneTask(String sceneTaskUuid) {
		QueryWrapper<SceneTask> queryWrapper = new QueryWrapper();
		queryWrapper.eq("uuid", sceneTaskUuid);
		SceneTask sceneTask = sceneTaskServie.getOne(queryWrapper);
		return sceneTask;
	}

	/**
	 * 根据巡查内容主键查找出预判处置部门id ,再根据部门id查出部门的人
	 * 
	 * @param sceneTaskUuid
	 * @return
	 */
	private List<Person> getDeptPersons(String sceneTaskUuid) {
		QueryWrapper<SceneTask> queryWrapper = new QueryWrapper();
		queryWrapper.select("dept_id");
		queryWrapper.eq("uuid", sceneTaskUuid);
		SceneTask sceneTask = sceneTaskServie.getOne(queryWrapper);
		// 根据部门id查询部门所有人
		QueryWrapper<Person> queryWrapperPerson = new QueryWrapper();
		queryWrapperPerson.eq("dept_id", sceneTask.getDeptId());
		List<Person> list = personServie.list();

		return list;

	}

	/**
	 * 邮件推送
	 * 
	 * @param map
	 */
	private void emailPush(Map map) {
		InputStream inStream = KafkaDataListener.class.getClassLoader().getResourceAsStream("conf/email.properties");
		Properties prop = new Properties();
		try {
			prop.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String emailAccount = prop.getProperty("email.account");
		String emailPassword = prop.getProperty("email.password");
		String fromAddress = prop.getProperty("email.from_address");
		String email = (String) map.get("email");
		String emailText = null;// 预警信息推送模板
		emailText = "预警信息：在" + map.get("warnAddress") + "," + map.get("warnContent") + ",请及时处理!预警时间："
				+ map.get("warnTime");
		// 配置，一次即可
		OhMyEmail.config(SMTP_QQ(true), emailAccount, emailPassword);
		try {
			OhMyEmail.subject(map.get("warnAddress") + "-" + map.get("warnContent")).from(fromAddress).to(email)
					.text(emailText).send();
		} catch (SendMailException e) {
			e.printStackTrace();
		}
	}

}
