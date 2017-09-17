package com.ghoulmind.habitica

import junit.framework.Assert.*
import org.junit.Test
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.WireMockServer
import org.junit.After
import org.junit.Before
import kotlin.test.assertFailsWith

class HabiticaClientTest {
    var client: HabiticaClient = HabiticaClient()
    val wireMockServer: WireMockServer = WireMockServer(options().dynamicPort().dynamicHttpsPort())

    @Before fun setUp() {
        wireMockServer.start()
        client = HabiticaClient("http://localhost:${wireMockServer.port()}/api/v3")
    }

    @After fun tearDown() {
        wireMockServer.stop()
    }

    @Test fun validLogin() {
        val response = aResponse().withStatus(200).withBody("""
                { "success": true, "data": { "id": "699caadf-38cf-4ad8-8eda-271d85b36e6x", "apiToken": "f64b38ef-a28c-49be-a9c9-26e4004988rc", "newUser":false }}
                """.trim())
        wireMockServer.stubFor(post(urlEqualTo("/api/v3/user/auth/local/login")).willReturn(response))
        val data = client.login("foo", "bar")
        assertEquals("699caadf-38cf-4ad8-8eda-271d85b36e6x", data.getString("id"))
        assertEquals("f64b38ef-a28c-49be-a9c9-26e4004988rc", data.getString("apiToken"))
    }

    @Test fun invalidLogin() {
        val response = aResponse().withStatus(400).withBody("""
                { "success": false, "error": "NotAuthorized", "message":"balabala" }
                """.trim())
        wireMockServer.stubFor(post(urlEqualTo("/api/v3/user/auth/local/login")).willReturn(response))

        assertFailsWith<HabiticaException> { client.login("foo", "bar") }
    }
}
