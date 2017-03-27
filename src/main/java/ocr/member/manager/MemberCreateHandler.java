package ocr.member.manager;

import io.vertx.core.http.HttpMethod;
import ocr.common.handler.SampleSingleDocBaseHandler;
import otocloud.common.ActionURI;
import otocloud.framework.app.function.ActionDescriptor;
import otocloud.framework.app.function.AppActivityImpl;
import otocloud.framework.app.function.BizRootType;
import otocloud.framework.app.function.BizStateSwitchDesc;
import otocloud.framework.core.HandlerDescriptor;

/*
 * 会员管理创建
 * 
 * @date 2016年11月20日
 * @author LCL
 */
//业务活动功能处理器
public class MemberCreateHandler extends SampleSingleDocBaseHandler {
	public static final String ADDRESS = "create";

	public MemberCreateHandler(AppActivityImpl appActivity) {
		super(appActivity);
	}

	/**
	 * corecorp_setting.setting
	 */
	@Override
	public String getEventAddress() {
		return ADDRESS;
	}

	@Override
	public ActionDescriptor getActionDesc() {

		ActionDescriptor actionDescriptor = super.getActionDesc();
		HandlerDescriptor handlerDescriptor = actionDescriptor.getHandlerDescriptor();

		// 外部访问url定义
		ActionURI uri = new ActionURI(getEventAddress(), HttpMethod.POST);
		handlerDescriptor.setRestApiURI(uri);

		// 状态变化定义
		BizStateSwitchDesc bizStateSwitchDesc = new BizStateSwitchDesc(BizRootType.BIZ_OBJECT, null,
				"created");
		bizStateSwitchDesc.setWebExpose(true); // 是否向web端发布事件
		actionDescriptor.setBizStateSwitch(bizStateSwitchDesc);

		return actionDescriptor;
	}

}
