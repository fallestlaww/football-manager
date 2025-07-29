import React, { useState, useEffect } from 'react';
import { getTeams, createTeam, updateTeam, deleteTeam } from '../services/api';
import TeamCard from '../components/TeamCard';
import TeamForm from '../components/TeamForm';

const TeamsPage = () => {
  const [teams, setTeams] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingTeam, setEditingTeam] = useState(null);

  useEffect(() => {
    fetchTeams();
  }, []);

  const fetchTeams = async () => {
    try {
      setLoading(true);
      const response = await getTeams();
      setTeams(response.data);
      setError(null);
    } catch (err) {
      setError('Error loading teams');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTeam = async (teamData) => {
    try {
      await createTeam(teamData);
      setShowForm(false);
      fetchTeams();
    } catch (err) {
      setError('Error creating team');
    }
  };

  const handleUpdateTeam = async (teamData) => {
    try {
      await updateTeam(editingTeam.id, teamData);
      setShowForm(false);
      setEditingTeam(null);
      fetchTeams();
    } catch (err) {
      setError('Error updating team');
    }
  };

  const handleDeleteTeam = async (id) => {
    if (window.confirm('Are you sure you want to delete this team?')) {
      try {
        await deleteTeam(id);
        fetchTeams();
      } catch (err) {
        setError('Error deleting team');
      }
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
      <div>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
          <h2 className="card-title">Teams</h2>
          <button
              className="btn btn-primary"
              onClick={() => {
                setEditingTeam(null);
                setShowForm(true);
                setError(null);
              }}
          >
            Create Team
          </button>
        </div>

        {error && <div className="alert alert-error">{error}</div>}

        {showForm && (
            <div className="card">
              <h3>{editingTeam ? 'Edit Team' : 'Create New Team'}</h3>
              <TeamForm
                  team={editingTeam}
                  onSubmit={editingTeam ? handleUpdateTeam : handleCreateTeam}
                  onCancel={() => {
                    setShowForm(false);
                    setEditingTeam(null);
                    setError(null);
                  }}
              />
            </div>
        )}

        <div className="list">
          {teams.map(team => (
              <TeamCard
                  key={team.id}
                  team={team}
                  onEdit={(team) => {
                    setEditingTeam(team);
                    setShowForm(true);
                    setError(null);
                  }}
                  onDelete={handleDeleteTeam}
              />
          ))}
        </div>
      </div>
  );
};

export default TeamsPage;
