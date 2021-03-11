package bio.relatives.common.config

import htsjdk.samtools.SamReaderFactory
import htsjdk.samtools.ValidationStringency
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Stores commonly used beans.
 * @author shvatov
 */
@Configuration
open class CommonBeans {
    @get:Bean("SamReaderFactory")
    open val samReaderFactory: SamReaderFactory = SamReaderFactory
        .makeDefault()
        .validationStringency(ValidationStringency.STRICT)
}