package interfaceApplication;

import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.java.JGrapeSystem.rMsg;
import common.java.apps.appsProxy;
import common.java.check.checkHelper;
import common.java.interfaceModel.GrapeDBDescriptionModel;
import common.java.interfaceModel.GrapePermissionsModel;
import common.java.interfaceModel.GrapeTreeDBModel;
import common.java.security.codec;
import common.java.session.session;
import common.java.string.StringHelper;
import main.java.interfaceApplication.userSimple;

/**
 * 维护单位
 * 
 *
 */
public class MCompany {
	
	private GrapeTreeDBModel MCompany;
	private JSONObject userInfo = null;
	private String currentWeb = null;
	private String pkStirng;

	public MCompany() {
		
		userSimple user = new userSimple();
		user.guesslogin(1200);

		MCompany = new GrapeTreeDBModel();
		//数据模型
		GrapeDBDescriptionModel  gDbSpecField = new GrapeDBDescriptionModel ();
		gDbSpecField.importDescription(appsProxy.tableConfig("MCompany"));
		MCompany.descriptionModel(gDbSpecField);
		
		//权限模型绑定
  		GrapePermissionsModel gperm = new GrapePermissionsModel(appsProxy.tableConfig("MCompany"));
  		MCompany.permissionsModel(gperm);
  		
  		pkStirng = MCompany.getPk();
		
		//用户信息
        userInfo = (new session()).getDatas();
		if (userInfo != null && userInfo.size() > 0) {
			currentWeb = userInfo.getString("currentWeb"); // 当前用户所属网站id
//			userType = userInfo.getInt("userType");//当前用户身份
		}
		
		//开启检查模式
		MCompany.enableCheck();
	}

	/**
	 * 新增维护单位信息
	 * 
	 * @param id
	 * @param info
	 * @return
	 */
	public String AddComp(String info) {
		String result = rMsg.netMSG(99, "新增失败");
//		info = checkParam(info);
		if(StringHelper.InvaildString(info)){
			result = rMsg.netMSG(1, "参数信息为空");
		}
		info = codec.DecodeFastJSON(info);
//		if (info.contains("errorcode")) {
//			return info;
//		}
		JSONObject object = JSONObject.toJSON(info);
		if (object != null && object.size() > 0) {
			info = (String) MCompany.data(object).insertEx();
			if(info != null){
				result = rMsg.netMSG(0, "新增成功");
			}
		}
		return result;
	}

	/**
	 * 修改维护单位信息
	 * 
	 * @param id
	 * @param info
	 * @return
	 */
	public String updateComp(String cid, String info) {
		boolean code = false;
		String result = rMsg.netMSG(100, "修改失败");
//		info = checkParam(info);
//		if (info.contains("errorcode")) {
//			return info;
//		}
		if(StringHelper.InvaildString(info)){
			result = rMsg.netMSG(99, "参数信息为空");
		}
		info = codec.DecodeFastJSON(info);
		
		JSONObject object = JSONObject.toJSON(info);
		if (ObjectId.isValid(cid) && object != null && object.size() > 0) {
			code = MCompany.eq(pkStirng, cid).data(object).updateEx();
			result = code ? rMsg.netMSG(0, "修改成功") : result;
		}
		return result;
	}

	/**
	 * 显示当前网站的维护单位信息
	 * @param idx
	 * @param pageSize
	 * @return
	 */
	public String PageMComp(int idx, int pageSize) {
		long total = 0;
		JSONArray array = null;
		if (!StringHelper.InvaildString(currentWeb)) {
			if (idx > 0 && pageSize > 0) {
				MCompany.eq("wbid", currentWeb);
				array = MCompany.dirty().page(idx, pageSize);
				total = MCompany.count();
			}
		}
		return rMsg.netPAGE(idx, pageSize, total, (array != null && array.size() > 0) ? array : new JSONArray());
	}

	/**
	 * 参数验证
	 * 
	 * @param info
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private String checkParam(String info) {
		String temp = "";
//		if (!StringHelper.InvaildString(info)) {// TODO 1
		if (StringHelper.InvaildString(info)) {
			return rMsg.netMSG(1, "参数错误");
		}
		JSONObject object = JSONObject.toJSON(info);
		if (object != null && object.size() > 0) {
			if (object.containsKey("companyEmail")) {
				temp = object.getString("companyEmail");
				if (!checkHelper.checkEmail(temp)) {
					return rMsg.netMSG(2, "邮箱格式错误");
				}
			}
			if (object.containsKey("companyMob")) {
				temp = object.getString("companyMob");
				if (!checkHelper.checkMobileNumber(temp)) {
					return rMsg.netMSG(3, "手机号格式错误");
				}
			}
			if (object.containsKey("companyURL")) {
				temp = object.getString("companyURL");
				temp = codec.DecodeHtmlTag(temp);
				temp = codec.decodebase64(temp);
				object.put("companyURL", temp);
			}
			object.put("wbid", currentWeb);
		}
		return (object != null && object.size() > 0) ? object.toString() : null;
	}
}
