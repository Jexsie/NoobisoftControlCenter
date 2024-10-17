FROM node:16 AS build
WORKDIR /app
ARG BACKEND_URL=http://localhost:5050
ENV REACT_APP_BACKEND_URL=${BACKEND_URL}
RUN echo "The Backend URL is '$REACT_APP_BACKEND_URL'"
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/nginx.conf
EXPOSE 8000
CMD ["nginx", "-g", "daemon off;"]
