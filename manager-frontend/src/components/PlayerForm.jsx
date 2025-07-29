import React, { useState, useEffect } from 'react';
import { getTeams } from '../services/api';

const PlayerForm = ({ player, onSubmit, onCancel }) => {
  const [formData, setFormData] = useState({
    first_name: '',
    last_name: '',
    age: '',
    months_of_experience: '',
    team_id: '',
    team_name: ''
  });

  const [teams, setTeams] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchTeams();
    if (player) {
      setFormData({
        first_name: player.first_name || '',
        last_name: player.last_name || '',
        age: player.age || '',
        months_of_experience: player.months_of_experience || '',
        team_id: player.team_id || '',
        team_name: player.team_name || ''
      });
    }
  }, [player]);

  const fetchTeams = async () => {
    try {
      const response = await getTeams();
      setTeams(response.data);
      setError(null);
    } catch (err) {
      setError('Error loading teams');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === 'team_id') {
      const selectedTeam = teams.find(team => team.id === parseInt(value)) || {};
      setFormData(prev => ({
        ...prev,
        team_id: parseInt(value) || '',
        team_name: selectedTeam.name || ''
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: name === 'age' || name === 'months_of_experience'
            ? parseInt(value) || 0
            : value
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      await onSubmit(formData);
    } catch (err) {
      let errorMessage = 'Error saving player';

      if (err.response) {
        const { status, statusText, data } = err.response;
        errorMessage = `${status} ${statusText}`;
        if (data?.message) {
          errorMessage += ` — ${data.message}`;
        }
      } else if (err.request) {
        errorMessage = 'No response from server';
      } else {
        errorMessage = err.message || 'Unknown error';
      }

      setError(errorMessage);
    }
  };

  return (
      <form onSubmit={handleSubmit}>
        {error && (
            <div className="alert alert-error">
              {error}
            </div>
        )}

        <div className="form-group">
          <label htmlFor="first_name">Name</label>
          <input
              type="text"
              id="first_name"
              name="first_name"
              value={formData.first_name}
              onChange={handleChange}
              required
          />
        </div>

        <div className="form-group">
          <label htmlFor="last_name">Surname:</label>
          <input
              type="text"
              id="last_name"
              name="last_name"
              value={formData.last_name}
              onChange={handleChange}
              required
          />
        </div>

        <div className="form-group">
          <label htmlFor="age">Age:</label>
          <input
              type="number"
              id="age"
              name="age"
              value={formData.age}
              onChange={handleChange}
              required
          />
        </div>

        <div className="form-group">
          <label htmlFor="months_of_experience">Experience (months):</label>
          <input
              type="number"
              id="months_of_experience"
              name="months_of_experience"
              value={formData.months_of_experience}
              onChange={handleChange}
              required
          />
        </div>

        <div className="form-group">
          <label htmlFor="team_id">Team:</label>
          <select
              id="team_id"
              name="team_id"
              value={formData.team_id}
              onChange={handleChange}
              required
          >
            <option value="">Choose team</option>
            {teams.map(team => (
                <option key={team.id} value={team.id}>{team.name}</option>
            ))}
          </select>
        </div>

        <button type="submit" className="btn btn-success">Save</button>
        <button type="button" className="btn btn-danger" onClick={onCancel}>Cancel</button>
      </form>
  );
};

export default PlayerForm;
