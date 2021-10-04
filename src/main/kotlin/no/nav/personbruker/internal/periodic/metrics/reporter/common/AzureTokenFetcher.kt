package no.nav.personbruker.internal.periodic.metrics.reporter.common

import no.nav.tms.token.support.azure.exchange.AzureServiceBuilder

class AzureTokenFetcher(private val eventHandlerAppEnvironmentDetails: String) {
    private val azureService = AzureServiceBuilder.buildAzureService(
            cachingEnabled = true,
            maxCachedEntries = 100,
            cacheExpiryMarginSeconds = 10
    )

    suspend fun getAccessToken(): String {
        return azureService.getAccessToken(eventHandlerAppEnvironmentDetails)
    }
}