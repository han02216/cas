---
layout: default
title: CAS - CAS REST Protocol
---

# REST Protocol
The REST protocol allows one to model applications as users, programmatically acquiring
service tickets to authenticate to other applications. This means that other applications would be able
to use a CAS client  to accept Service Tickets rather than to rely upon another technology such as
client SSL certificates for application-to-application authentication of requests. This is achieved
by exposing a way to RESTfully obtain a Ticket Granting Ticket and then use that to obtain a Service Ticket.

<div class="alert alert-warning"><strong>Usage Warning!</strong><p>The REST endpoint may
 become a tremendously convenient target for brute force dictionary attacks on CAS server. Enable support
 only soberly and with due consideration of security aspects.</p></div>

# Components
By default the CAS REST API is configured to add routing for the tickets. It

also defines the resources that will resolve the URLs. The `TicketResource` defined by
default (which can be extended) accepts username/password.

Support is enabled by including the following in your `pom.xml` file:

{% highlight xml %}
<dependency>
    <groupId>org.jasig.cas</groupId>
    <artifactId>cas-server-support-rest</artifactId>
    <version>${cas.version}</version>
    <scope>runtime</scope>
</dependency>
{% endhighlight %}

REST support is currently provided internally by the [Spring framework](http://spring.io/guides/gs/rest-service/‎).

#Protocol

##Request a Ticket Granting Ticket

###Sample Request
{% highlight bash %}
POST /cas/v1/tickets HTTP/1.0

username=battags&password=password&additionalParam1=paramvalue
{% endhighlight %}

###Sample Response

####Successful Response
{% highlight bash %}
201 Created
Location: http://www.whatever.com/cas/v1/tickets/{TGT id}
{% endhighlight %}

####Unsuccessful Response
If incorrect credentials are sent, CAS will respond with a 400 Bad Request error
(will also respond for missing parameters, etc.). If you send a media type
it does not understand, it will send the 415 Unsupported Media Type.

##Request a Service Ticket

###Sample Request
{% highlight bash %}
POST /cas/v1/tickets/{TGT id} HTTP/1.0

service={form encoded parameter for the service url}
{% endhighlight %}

###Sample Response

####Successful Response
{% highlight bash %}
200 OK
ST-1-FFDFHDSJKHSDFJKSDHFJKRUEYREWUIFSD2132
{% endhighlight %}
####Unsuccessful Response
CAS will send a 400 Bad Request. If an incorrect media type is
sent, it will send the 415 Unsupported Media Type.

##Logout
{% highlight bash %}
DELETE /cas/v1/tickets/TGT-fdsjfsdfjkalfewrihfdhfaie HTTP/1.0
{% endhighlight %}

##Add Service
Invoke CAS to register applications into its own service registry. The REST
call must be authenticated as it requires a TGT from the CAS server, and furthermore,
the authenticated principal that submits the request must be authorized with a
preconfigured role name and value that is designated in the CAS configuration
via the following properties:

{% highlight properties %}
# cas.rest.services.attributename=
# cas.rest.services.attributevalue=
{% endhighlight %}

{% highlight bash %}
POST /cas/v1/services/add/{TGT id} HTTP/1.0
serviceId=svcid&name=svcname&description=svcdesc&evaluationOrder=1234&enabled=true&ssoEnabled=true
{% endhighlight %}

####Successful Response
If the request is successful, the returned value in the response would be
the generated identifier of the new service.

{% highlight bash %}
200 OK
5463544213
{% endhighlight %}
