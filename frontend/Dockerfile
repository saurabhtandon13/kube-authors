#FROM node:13-alpine
FROM node:13-slim

RUN mkdir -p /home/node/app/node_modules && chown -R node:node /home/node/app
WORKDIR /home/node/app
COPY --chown=node:node ./frontend /home/node/app

USER node
# Environment variables
ENV NODE_ENV production
ENV PORT 3000
ENV PUBLIC_PATH "/"

RUN npm install

EXPOSE 3000

## hack to ensure this works well when we use build pack, check package.json and script-hack.sh 
CMD [ "npm", "dev" ]
