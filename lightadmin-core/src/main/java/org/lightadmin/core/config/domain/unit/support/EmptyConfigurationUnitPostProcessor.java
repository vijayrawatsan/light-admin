package org.lightadmin.core.config.domain.unit.support;

import org.lightadmin.api.config.builder.FieldSetConfigurationUnitBuilder;
import org.lightadmin.api.config.unit.FieldSetConfigurationUnit;
import org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType;
import org.lightadmin.core.config.domain.common.GenericFieldSetConfigurationUnitBuilder;
import org.lightadmin.core.config.domain.unit.ConfigurationUnit;
import org.lightadmin.core.config.domain.unit.ConfigurationUnits;
import org.lightadmin.core.persistence.metamodel.DomainTypeAttributeMetadata;
import org.lightadmin.core.persistence.metamodel.DomainTypeEntityMetadataResolver;
import org.springframework.util.ClassUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.sort;
import static org.lightadmin.core.config.bootstrap.parsing.configuration.DomainConfigurationUnitType.CONFIGURATION;
import static org.lightadmin.core.config.domain.field.FieldMetadataUtils.DomainTypeAttributeMetadataComparator;
import static org.lightadmin.core.persistence.metamodel.DomainTypeAttributeType.isSupportedAttributeType;

public class EmptyConfigurationUnitPostProcessor extends EntityMetadataResolverAwareConfigurationUnitPostProcessor {

    public EmptyConfigurationUnitPostProcessor(final DomainTypeEntityMetadataResolver entityMetadataResolver) {
        super(entityMetadataResolver);
    }

    @Override
    public ConfigurationUnit postProcess(final ConfigurationUnit configurationUnit, ConfigurationUnits configurationUnits) {
        if (configurationUnit.getDomainConfigurationUnitType() != CONFIGURATION && isEmptyFieldSetConfigurationUnit(configurationUnit)) {
            return fieldSetUnitWithPersistentFields(configurationUnit.getDomainType(), configurationUnit.getDomainConfigurationUnitType());
        }

        return configurationUnit;
    }

    private FieldSetConfigurationUnit fieldSetUnitWithPersistentFields(final Class<?> domainType, DomainConfigurationUnitType configurationUnitType) {
        FieldSetConfigurationUnitBuilder fieldSetConfigurationUnitBuilder = new GenericFieldSetConfigurationUnitBuilder(domainType, configurationUnitType);

        final List<DomainTypeAttributeMetadata> attributes = newArrayList(resolveEntityMetadata(domainType).getAttributes());

        sort(attributes, new DomainTypeAttributeMetadataComparator());

        for (DomainTypeAttributeMetadata attribute : attributes) {
            if (isSupportedAttributeType(attribute.getAttributeType())) {
                fieldSetConfigurationUnitBuilder.field(attribute.getName());
            }
        }

        return fieldSetConfigurationUnitBuilder.build();
    }

    private boolean isEmptyFieldSetConfigurationUnit(ConfigurationUnit configurationUnit) {
        return ClassUtils.isAssignableValue(FieldSetConfigurationUnit.class, configurationUnit) && ((FieldSetConfigurationUnit) configurationUnit).isEmpty();
    }
}
