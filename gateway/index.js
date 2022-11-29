const { ApolloServer } = require("apollo-server");
const { ApolloGateway , RemoteGraphQLDataSource } = require("@apollo/gateway");

class AuthenticatedDataSource extends RemoteGraphQLDataSource {
  willSendRequest({ request, context }) {
    // Pass the JWT token from the context to underlying services
    request.http.headers.set(   'Authorization', context.token);
  }
}
const gateway = new ApolloGateway({
    debug: true,
    serviceList: [
//      { name: "auth", url: "http://localhost:8081/graphql" },
//      { name: "mdm", url: "http://localhost:8082/graphql" },
      { name: "catalog", url: "http://localhost:8083/graphql" },
      { name: "inventory", url: "http://localhost:8084/graphql" },
      { name: "price", url: "http://localhost:8085/graphql" },
      { name: "promotion", url: "http://localhost:8086/graphql" },
      { name: "order", url: "http://localhost:8087/graphql" }
    ],
    buildService({ name, url }) {
       return new AuthenticatedDataSource({ url });
    }
});
const server = new ApolloServer( {
    gateway,
    subscriptions: false,
    tracing:true ,

    context: ({ req }) => {
      const token = req.headers.authorization || '';
      return {  token };
    }
});
server.listen().then(({ url }) => {
  console.log(`🚀 Server ready at ${url}`);
});