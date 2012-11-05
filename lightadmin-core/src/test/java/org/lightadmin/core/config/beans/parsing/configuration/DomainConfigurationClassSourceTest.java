package org.lightadmin.core.config.beans.parsing.configuration;

import org.junit.Test;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.lightadmin.core.test.util.DummyConfigurationsHelper.*;

public class DomainConfigurationClassSourceTest {

	private DomainConfigurationClassSource subject;

	@Test( expected = IllegalArgumentException.class )
	public void nullDomainTypeEntityMetadataNotAllowed() {
		subject = new DomainConfigurationClassSource( null, DomainEntityEmptyConfiguration.class );
	}

	@Test( expected = IllegalArgumentException.class )
	public void nullConfigurationClassNotAllowed() {
		subject = new DomainConfigurationClassSource( domainTypeEntityMetadataMock( DomainEntity.class ), null );
	}

	@Test
	public void correctDomainTypeReturned() {
		subject = emptyDomainEntityConfiguration();

		assertEquals( DomainEntity.class, subject.getDomainType() );
	}

	@Test
	public void correctEntityMetadataReturned() throws Exception {
		final DomainTypeEntityMetadata expectedMetadata = domainTypeEntityMetadataMock( DomainEntity.class );

		subject = new DomainConfigurationClassSource( expectedMetadata, DomainEntityEmptyConfiguration.class );

		assertEquals( expectedMetadata, subject.getDomainTypeEntityMetadata() );
	}

	@Test
	public void equalDomainConfigurations() throws Exception {
		final DomainConfigurationClassSource domainConfigurationClassSource = new DomainConfigurationClassSource( domainTypeEntityMetadataMock( DomainEntity.class ), DomainEntityEmptyConfiguration.class );
		final DomainConfigurationClassSource otherDomainConfigurationClassSource = new DomainConfigurationClassSource( domainTypeEntityMetadataMock( DomainEntity.class ), DomainEntityEmptyConfiguration.class );

		assertEquals( domainConfigurationClassSource, otherDomainConfigurationClassSource );
	}

	@Test
	public void notEqualDomainConfigurations() throws Exception {
		final DomainConfigurationClassSource domainConfigurationClassSource = new DomainConfigurationClassSource( domainTypeEntityMetadataMock( Object.class ), DomainEntityEmptyConfiguration.class );
		final DomainConfigurationClassSource otherDomainConfigurationClassSource = new DomainConfigurationClassSource( domainTypeEntityMetadataMock( DomainEntity.class ), DomainEntityEmptyConfiguration.class );

		assertFalse( domainConfigurationClassSource.equals( otherDomainConfigurationClassSource ) );
	}
}