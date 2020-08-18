package io.github.newlight77.core

import io.github.newlight77.core.email.EmailService
import io.github.newlight77.core.login.LoginJpaRepository
import io.github.newlight77.core.login.SignupJpaRepository
import io.github.newlight77.core.metafile.MetafileRepository
import io.github.newlight77.core.okta.OktaService
import io.github.newlight77.core.twilio.SmsService
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.ActiveProfiles

@ComponentScan("io.github.newlight77.core")
@ActiveProfiles("test")
class InfrastructureMockBeans {
    
    @MockBean
    lateinit var oktaService: OktaService

    @MockBean
    lateinit var emailService: EmailService

    @MockBean
    lateinit var smsService: SmsService

    @MockBean
    lateinit var metaFileRepository: MetafileRepository
}