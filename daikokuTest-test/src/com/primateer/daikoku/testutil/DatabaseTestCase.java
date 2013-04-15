package com.primateer.daikoku.testutil;

import com.primateer.daikoku.Application;
import com.primateer.daikoku.db.DatabaseProvider;

import android.test.ProviderTestCase2;

public class DatabaseTestCase extends ProviderTestCase2<DatabaseProvider> {

	public DatabaseTestCase() {
		super(DatabaseProvider.class, DatabaseProvider.AUTHORITY);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		Application.setAlternateContext(getMockContext());
	}
}
