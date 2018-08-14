package com.bj.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bj.dao.mapper.TRegisterLogMapper;
import com.bj.pojo.TRegisterLog;
import com.bj.util.UnicodeUtil;

import net.sf.json.JSONObject;

@Service
public class QueryAreaJob{
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryAreaJob.class);
	@Resource
	private TRegisterLogMapper tRegisterLogMapper;
	
	public class BaiduApiJob implements Runnable{
		
		private String apiUrl = "";
		private String ip = "";
		private int userType = 1;
		private String macCpu = "";
		
		public BaiduApiJob(String apiUrl, String ip, int userType, String macCpu) {
			this.apiUrl = apiUrl.replace("{ip}", ip);
			this.ip = ip;
			this.userType = userType;
			this.macCpu = macCpu;
		}
	
		@Override
		public void run() {
			try {
				String result = callApi();
				JSONObject obj = JSONObject.fromObject(result);
				int status = obj.getInt("status");
				if(status == 0) {
					String add = obj.getString("address");
					String nation = "CN";
					if(add.split("\\|").length > 1) {
						nation = add.split("\\|")[0];
					}
					JSONObject detailObj = obj.getJSONObject("content").getJSONObject("address_detail");
					String province = detailObj.getString("province");
					String city = detailObj.getString("city");
					String district = detailObj.getString("district");
					JSONObject pointObj = obj.getJSONObject("content").getJSONObject("point");
					String x = pointObj.getString("x");
					String y = pointObj.getString("y");
					
					TRegisterLog log = new TRegisterLog();
					log.setIp(ip);
					log.setUserType(userType);
					log.setNation(nation);
					log.setProvince(province);
					log.setCity(city);
					log.setDistrict(district);
					log.setX(x);
					log.setY(y);
					log.setCreateTime(new Date());
					log.setMacCpu(macCpu);
					tRegisterLogMapper.insert(log);
				}else {
					LOGGER.error("IP[{}]返回失败：{}", ip, status);
				}
			} catch (IOException e) {
				LOGGER.error("获取失败：{}", e);
			}		
		}
	
		/**
		 * 接口错误码
		 * 状态码	定义	                                注释
			0	正常	
			1	服务器内部错误	                            该服务响应超时或系统内部错误，如遇此问题，请到官方论坛进行反馈
			10	上传内容超过8M	                            Post上传数据不能超过8M
			101	AK参数不存在	                            请求消息没有携带AK参数
			102	MCODE参数不存在，mobile类型mcode参数必需	对于Mobile类型的应用请求需要携带mcode参数，该错误码代表服务器没有解析到mcode
			200	APP不存在，AK有误请检查再重试	            根据请求的ak，找不到对应的APP
			201	APP被用户自己禁用，请在控制台解禁	
			202	APP被管理员删除	恶意APP被管理员删除
			203	APP类型错误	                                当前API控制台支持Server(类型1), Mobile(类型2, 新版控制台区分为Mobile_Android(类型21)及Mobile_IPhone（类型22）及Browser（类型3），除此之外的其他类型被认为是APP类型错误
			210	APP IP校验失败	                            在申请Server类型应用的时候选择IP校验，需要填写IP白名单，如果当前请求的IP地址不在IP白名单或者不是0.0.0.0/0就认为IP校验失败
			211	APP SN校验失败	                            SERVER类型APP有两种校验方式：IP校验和SN校验，当用户请求的SN和服务端计算出来的SN不相等的时候，提示SN校验失败
			220	APP Referer校验失败	                        浏览器类型的APP会校验referer字段是否存在，且在referer白名单里面，否则返回该错误码
			230	APP Mcode码校验失败	                        服务器能解析到mcode，但和数据库中不一致，请携带正确的mcode
			240	APP 服务被禁用	                            用户在API控制台中创建或设置某APP的时候禁用了某项服务
			250	用户不存在	                                根据请求的user_id, 数据库中找不到该用户的信息，请携带正确的user_id
			251	用户被自己删除	                            该用户处于未激活状态
			252	用户被管理员删除	                        恶意用户被加入黑名单
			260	服务不存在	                                服务器解析不到用户请求的服务名称
			261	服务被禁用	                                该服务已下线
			301	永久配额超限，限制访问	                    配额超限，如果想增加配额请联系我们
			302	天配额超限，限制访问	                    配额超限，如果想增加配额请联系我们
			401	当前并发量已经超过约定并发配额，限制访问	并发控制超限，请控制并发量请联系我们
			402	当前并发量已经超过约定并发配额，并且服务总并发量也已经超过设定的总并发配额，限制访问	并发控制超限，请控制并发量请联系我们
		 * @throws IOException 
		 */
		private String callApi() throws IOException {
	        InputStream in = null;
	        InputStreamReader isr = null;
	        BufferedReader br = null;
			try {
	            URL url = new URL(apiUrl);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.addRequestProperty("encoding", "UTF-8");//添加请求属性
	            connection.setDoOutput(true);//允许输出
	            connection.setRequestMethod("GET");
	            
	            in = connection.getInputStream();
	            isr = new InputStreamReader(in,"UTF-8");
	            br = new BufferedReader(isr);
	            
	            String line;
	            StringBuilder sb = new StringBuilder();
	            while((line = br.readLine()) != null) {
	                sb.append(line);
	            }
	            
	            String result = UnicodeUtil.unicode2String(sb.toString());
	            LOGGER.info("IP[{}]解析地址：{}",ip,result);
	            return result;
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            br.close();
	            isr.close();
	            in.close();
			}
			return null;
		}
	}
	
	public static void main(String[] args) {
		QueryAreaJob query = new QueryAreaJob();
		BaiduApiJob job = query.new BaiduApiJob("http://api.map.baidu.com/location/ip?ip={ip}&ak=gLKkHiGfgILxG4RymXcOUooxU8z9xG0b&coor=bd09ll","202.100.192.68",1,"");
		Thread  t = new Thread(job);
		t.start();
	}
}
