package ocr.member.manager;

import java.util.List;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import otocloud.common.ActionURI;
import otocloud.framework.app.function.ActionDescriptor;
import otocloud.framework.app.function.ActionHandlerImpl;
import otocloud.framework.app.function.AppActivityImpl;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;

/**
 * 会员查询
 * 
 * @date 2016年11月26日
 * @author
 */
public class MemberQueryHandler extends ActionHandlerImpl<JsonObject> {

	public static final String ADDRESS = "query";

	public MemberQueryHandler(AppActivityImpl appActivity) {
		super(appActivity);

	}

	// 此action的入口地址
	@Override
	public String getEventAddress() {
		// TODO Auto-generated method stub
		return ADDRESS;
	}

	// 处理器
	@Override
	public void handle(OtoCloudBusMessage<JsonObject> msg) {

		JsonObject query = msg.body();
	
		appActivity.getAppDatasource().getMongoClient()
				.findWithOptions(appActivity.getDBTableName(this.appActivity.getBizObjectType()), query, new FindOptions(), findRet -> {
					if (findRet.succeeded()) {
						List<JsonObject> retObj = findRet.result();
						if(retObj != null && retObj.size() > 0)
						{
							msg.reply(retObj.get(0));
						}else{
							msg.reply(null);
						}
					} else {
						Throwable err = findRet.cause();
						String errMsg = err.getMessage();
						appActivity.getLogger().error(errMsg, err);
						msg.fail(500, errMsg);
					}

				});

	}

	/**
	 * 此action的自描述元数据
	 */
	@Override
	public ActionDescriptor getActionDesc() {

		ActionDescriptor actionDescriptor = super.getActionDesc();
		HandlerDescriptor handlerDescriptor = actionDescriptor.getHandlerDescriptor();

		ActionURI uri = new ActionURI(ADDRESS, HttpMethod.POST);
		handlerDescriptor.setRestApiURI(uri);

		return actionDescriptor;
	}

}
