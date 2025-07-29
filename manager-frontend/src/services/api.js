import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});


export const getTeams = () => api.get('/team/list');
export const getTeam = (id) => api.get(`/team/${id}`);
export const createTeam = (team) => api.post('/team/create', team);
export const updateTeam = (id, team) => api.put(`/team/${id}`, team);
export const deleteTeam = (id) => api.delete(`/team/${id}`);

export const getPlayers = () => api.get('/player/list');
export const getPlayer = (id) => api.get(`/player/${id}`);
export const createPlayer = (player) => api.post('/player/create', player);
export const updatePlayer = (id, player) => api.put(`/player/${id}`, player);
export const deletePlayer = (id) => api.delete(`/player/${id}`);

export const transferPlayer = (playerId, newTeamId) => api.post(`/transfer/${playerId}/to/${newTeamId}`);

export default api;
