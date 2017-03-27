package ocr.member.manager;

import java.util.ArrayList;
import java.util.List;

import otocloud.framework.app.function.AppActivityImpl;
import otocloud.framework.app.function.BizRoleDescriptor;
import otocloud.framework.core.OtoCloudEventDescriptor;
import otocloud.framework.core.OtoCloudEventHandlerRegistry;

/**
 * 会员管理
 * 
 * @date 2016年11月26日
 * @author
 */
public class MemberComponent extends AppActivityImpl {

	// 业务活动组件名
	@Override
	public String getName() {
		return "member-mgr";
	}

	// 业务活动组件要处理的核心业务对象
	@Override
	public String getBizObjectType() {
		return "ba_member";
	}

	// 发布此业务活动关联的业务角色
	@Override
	public List<BizRoleDescriptor> exposeBizRolesDesc() {

		BizRoleDescriptor bizRole = new BizRoleDescriptor("2", "核心企业");

		List<BizRoleDescriptor> ret = new ArrayList<BizRoleDescriptor>();
		ret.add(bizRole);
		return ret;
	}

	// 发布此业务活动对外暴露的业务事件
	@Override
	public List<OtoCloudEventDescriptor> exposeOutboundBizEventsDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	// 业务活动组件中的业务功能
	@Override
	public List<OtoCloudEventHandlerRegistry> registerEventHandlers() {

		List<OtoCloudEventHandlerRegistry> ret = new ArrayList<OtoCloudEventHandlerRegistry>();

		MemberQueryHandler queryHandler = new MemberQueryHandler(this);
		ret.add(queryHandler);

		MemberCreateHandler createHandler = new MemberCreateHandler(this);
		ret.add(createHandler);

		MemberUpdateHandler updateHandle = new MemberUpdateHandler(this);
		ret.add(updateHandle);

		return ret;
	}

}
