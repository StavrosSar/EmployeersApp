FROM node:22-bookworm-slim AS build
WORKDIR /app

RUN corepack enable

COPY package.json package-lock.json ./
RUN npm ci

COPY . .
RUN npm run build

RUN mkdir -p /tmp/dist \
  && if [ -d "dist/employeemanagerapp/browser" ]; then cp -R dist/employeemanagerapp/browser/* /tmp/dist/; \
     else cp -R dist/employeemanagerapp/* /tmp/dist/; \
     fi

FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /tmp/dist/ /usr/share/nginx/html/

EXPOSE 80
