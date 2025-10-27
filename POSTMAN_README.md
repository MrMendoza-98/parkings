# 📋 Parkings API - Postman Collection

## 🎯 Descripción
Colección completa de Postman para documentar y probar la API de gestión de parqueaderos con autenticación JWT, roles ADMIN/SOCIO, y funcionalidades avanzadas de indicadores.

## 📁 Archivos Incluidos

### 1. `Parkings_API_Collection.postman_collection.json`
Colección principal con **todos los endpoints** organizados por módulos:

- 🔐 **Authentication** (Login ADMIN/SOCIO, Logout)
- 👥 **Users Management** (CRUD con validaciones)
- 🅿️ **Parkings Management** (CRUD con control de propiedad)
- 🚗 **Vehicles Management** (Entrada/Salida, consultas)
- 📊 **Indicators & Analytics** (Estadísticas y reportes)

### 2. `Parkings_API_Development.postman_environment.json`
Variables de entorno para desarrollo local con configuraciones predefinidas.

## 🚀 Cómo Importar en Postman

### Paso 1: Importar la Colección
1. Abre Postman
2. Click en **Import**
3. Selecciona `Parkings_API_Collection.postman_collection.json`
4. Click **Import**

### Paso 2: Importar el Environment
1. Click en **Import** nuevamente
2. Selecciona `Parkings_API_Development.postman_environment.json`
3. Click **Import**

### Paso 3: Activar el Environment
1. En la esquina superior derecha, selecciona **"Parkings API - Development"**
2. Verifica que `baseUrl` esté configurado como `http://localhost:8080`

## 🔐 Autenticación Automática

### Configuración Initial
1. **Ejecuta el endpoint**: `🔐 Authentication > Login - Admin`
2. **Automáticamente se guardará**:
   - Token JWT en variable `{{authToken}}`
   - User ID, Email y Role
3. **Todos los demás endpoints** usarán automáticamente el token

### Test Scripts Incluidos
```javascript
// Auto-save del token después del login
if (pm.response.code === 200) {
    const responseJson = pm.response.json();
    pm.environment.set('authToken', responseJson.token);
    pm.environment.set('userId', responseJson.user.id);
    pm.environment.set('userEmail', responseJson.user.email);
    pm.environment.set('userRole', responseJson.user.role);
}
```

## 📋 Estructura de la Colección

### 🔐 Authentication
- **Login - Admin**: `POST /auth/login` (admin@parkings.com)
- **Login - Socio**: `POST /auth/login` (socio@parkings.com)  
- **Logout**: `POST /auth/logout`

### 👥 Users Management (Solo ADMIN)
- **Get All Users**: `GET /users`
- **Get User by ID**: `GET /users/{id}`
- **Create User**: `POST /users`
- **Update User**: `PUT /users/{id}`
- **Delete User**: `DELETE /users/{id}` ⚠️ *Con validación de parqueaderos*

### 🅿️ Parkings Management
- **Get All Parkings**: `GET /parkings` (ADMIN: todos, SOCIO: propios)
- **Get Parking by ID**: `GET /parkings/{id}`
- **Create Parking**: `POST /parkings`
- **Update Parking**: `PUT /parkings/{id}` ⚠️ *Con validación de capacidad*
- **Delete Parking**: `DELETE /parkings/{id}` ⚠️ *Con validación histórica*

### 🚗 Vehicles Management
- **Register Entry**: `POST /vehicles/register-entry`
- **Register Exit**: `POST /vehicles/register-exit`
- **Get Parked Vehicles**: `GET /vehicles/parked/{parkingId}`
- **Get Vehicle Info**: `GET /vehicles/info/{plate}`
- **Validate Vehicle**: `GET /vehicles/validate/{plate}/{parkingId}`

### 📊 Indicators & Analytics
- **Top Vehicles (All)**: `GET /indicators/top-vehicles`
- **Top Vehicles by Parking**: `GET /indicators/top-vehicles/parking/{id}`
- **First Time Vehicles**: `GET /indicators/first-time-vehicles/parking/{id}`
- **Parking Earnings**: `GET /indicators/earnings/{id}` (Solo SOCIO propietario)
- **Search by Plate**: `GET /indicators/search-vehicles?partialPlate=ABC`

## 💡 Ejemplos de Respuestas Incluidas

### ✅ Respuestas Exitosas
Cada endpoint incluye ejemplos de respuestas exitosas con datos reales.

### ❌ Casos de Error
Incluye ejemplos de validaciones de negocio:
- Usuario con parqueaderos asociados
- Capacidad insuficiente
- Token expirado
- Permisos insuficientes

## 🎯 Flujo de Pruebas Recomendado

### 1. Autenticación
```
1. Login Admin → Guarda token automáticamente
2. Probar endpoints de gestión
3. Login Socio → Cambiar contexto
4. Probar endpoints con permisos limitados
```

### 2. Gestión de Datos
```
1. Crear usuarios (ADMIN)
2. Crear parqueaderos
3. Registrar vehículos
4. Generar movimientos
5. Consultar indicadores
```

### 3. Validaciones de Negocio
```
1. Intentar eliminar usuario con parqueaderos
2. Reducir capacidad con vehículos activos
3. Eliminar parqueadero con historial
4. Acceder a ganancias sin permisos
```

## ⚙️ Variables de Entorno

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `baseUrl` | URL base de la API | `http://localhost:8080` |
| `authToken` | JWT Token (auto-generado) | `eyJhbGciOiJIUzI1NiIs...` |
| `userId` | ID del usuario logueado | `1` |
| `userEmail` | Email del usuario logueado | `admin@parkings.com` |
| `userRole` | Rol del usuario logueado | `ADMIN` |

## 🔧 Personalización

### Cambiar URL de Producción
```json
"baseUrl": "https://api.parkings.prod.com"
```

### Agregar Nuevos Endpoints
1. Duplicar request existente
2. Modificar URL y método
3. Actualizar body/headers según necesidad

## 📞 Soporte
- **Documentación completa** incluida en cada request
- **Ejemplos de respuesta** para casos exitosos y de error
- **Scripts de automatización** para flujos comunes

---

🚀 **¡Listo para probar tu API completa!** 🚀