#FROM node:13-alpine
FROM node:13-slim

RUN mkdir -p /home/node/app/node_modules && chown -R node:node /home/node/app
WORKDIR /home/node/app
COPY --chown=node:node . .

USER node
# Environment variables
ENV NODE_ENV production
ENV PORT 3000
ENV PUBLIC_PATH "/"
RUN npm install

EXPOSE 3000
CMD [ "npm", "start" ]
