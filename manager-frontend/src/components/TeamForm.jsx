import React, { useState, useEffect } from 'react';

const TeamForm = ({ team, onSubmit, onCancel }) => {
    const [formData, setFormData] = useState({
        name: '',
        country: '',
        balance: '',
        commission_rate: ''
    });

    const [error, setError] = useState(null);

    useEffect(() => {
        if (team) {
            setFormData({
                name: team.name || '',
                country: team.country || '',
                balance: team.balance || '',
                commission_rate: team.commission_rate || ''
            });
        }
    }, [team]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'balance' || name === 'commission_rate'
                ? parseFloat(value) || 0
                : value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        try {
            await onSubmit(formData);
        } catch (err) {
            let errorMessage = 'Error saving team';

            if (err.response) {
                const { status, statusText } = err.response;
                errorMessage = `${status} ${statusText}`;
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
                <label htmlFor="name">Team name:</label>
                <input
                    type="text"
                    id="name"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="form-group">
                <label htmlFor="country">Country:</label>
                <input
                    type="text"
                    id="country"
                    name="country"
                    value={formData.country}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="form-group">
                <label htmlFor="balance">Balance ($):</label>
                <input
                    type="number"
                    id="balance"
                    name="balance"
                    value={formData.balance}
                    onChange={handleChange}
                    min="0"
                    required
                />
            </div>

            <div className="form-group">
                <label htmlFor="commission_rate">Commission rate (%):</label>
                <input
                    type="number"
                    id="commission_rate"
                    name="commission_rate"
                    value={formData.commission_rate}
                    onChange={handleChange}
                    min="0"
                    max="100"
                    step="0.001"
                    required
                />
            </div>

            <button type="submit" className="btn btn-success">Save</button>
            <button type="button" className="btn btn-danger" onClick={onCancel}>Cancel</button>
        </form>
    );
};

export default TeamForm;
