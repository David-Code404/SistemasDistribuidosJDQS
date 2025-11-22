const express = require('express');
const { graphqlHTTP } = require('express-graphql');
const { buildSchema } = require('graphql');
const cors = require('cors');

// Datos simulados (Base de datos)
const personas = [
    { ci: "12345", nombres: "Juan", primerApellido: "Perez", segundoApellido: "Lopez" },
    { ci: "67890", nombres: "Maria", primerApellido: "Gomez", segundoApellido: "Arce" }
];

// Esquema GraphQL
const schema = buildSchema(`
    type Persona {
        ci: String
        nombres: String
        primerApellido: String
        segundoApellido: String
    }
    type Query {
        persona(ci: String!): Persona
    }
`);

// Resolvers
const root = {
    persona: ({ ci }) => {
        return personas.find(p => p.ci === ci);
    }
};

const app = express();
app.use(cors());
app.use('/graphql', graphqlHTTP({
    schema: schema,
    rootValue: root,
    graphiql: true,
}));

app.listen(4000, () => console.log('SEGIP GraphQL corriendo en http://localhost:4000/graphql'));