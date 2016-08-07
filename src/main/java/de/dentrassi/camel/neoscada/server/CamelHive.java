package de.dentrassi.camel.neoscada.server;

import static java.util.Collections.emptyMap;

import org.eclipse.scada.da.server.browser.common.FolderCommon;
import org.eclipse.scada.da.server.browser.common.query.GroupFolder;
import org.eclipse.scada.da.server.browser.common.query.GroupProvider;
import org.eclipse.scada.da.server.browser.common.query.IDNameProvider;
import org.eclipse.scada.da.server.browser.common.query.InvisibleStorage;
import org.eclipse.scada.da.server.browser.common.query.ItemDescriptor;
import org.eclipse.scada.da.server.browser.common.query.NameProvider;
import org.eclipse.scada.da.server.browser.common.query.SplitGroupProvider;
import org.eclipse.scada.da.server.browser.common.query.SplitNameProvider;
import org.eclipse.scada.da.server.common.DataItem;
import org.eclipse.scada.da.server.common.impl.HiveCommon;

public class CamelHive extends HiveCommon {
	private FolderCommon root;
	private InvisibleStorage storage;
	private GroupFolder group;

	@Override
	protected void performStart() throws Exception {
		setRootFolder(this.root = new FolderCommon());
		this.storage = new InvisibleStorage();

		final GroupProvider groupProvider = new SplitGroupProvider(new IDNameProvider(), "\\.");
		final NameProvider nameProvider = new SplitNameProvider(new IDNameProvider(), "\\.", -1, 1, ".");

		this.group = new GroupFolder(groupProvider, nameProvider);
		this.storage.addChild(this.group);

		this.root.add("All Items", this.group, null);
	}

	@Override
	public void registerItem(final DataItem item) {
		super.registerItem(item);
		this.storage.added(new ItemDescriptor(item, emptyMap()));
	}

	@Override
	public void unregisterItem(final DataItem item) {
		this.storage.removed(new ItemDescriptor(item, emptyMap()));
		super.unregisterItem(item);
	}

	@Override
	public String getHiveId() {
		return "camel";
	}

}
