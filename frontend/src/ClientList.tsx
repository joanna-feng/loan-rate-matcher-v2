import { useEffect, useState } from 'react'

interface Client {
  id: number
  name: string
  creditScore: number
  loanRate: number
}

function ClientList() {
  const [clients, setClients] = useState<Client[]>([])
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetch('http://localhost:8080/api/clients')
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Request failed with status ${response.status}`)
        }
        return response.json()
      })
      .then((data: Client[]) => setClients(data))
      .catch(() => setError('Could not load clients. Is the backend running?'))
  }, [])

  if (error) {
    return <p className="error">{error}</p>
  }

  return (
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Credit Score</th>
          <th>Loan Rate</th>
        </tr>
      </thead>
      <tbody>
        {clients.map((client) => (
          <tr key={client.id}>
            <td>{client.id}</td>
            <td>{client.name}</td>
            <td>{client.creditScore}</td>
            <td>{client.loanRate}%</td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}

export default ClientList
